-- =====================================================
-- SUPABASE AUTH MIGRATION SCRIPT
-- Phase 2: Database Schema Updates
-- =====================================================
-- This script updates the public.users table to integrate with Supabase Auth
-- Run this in your Supabase SQL Editor

-- Step 1: Add auth_user_id column to public.users table
-- This links our custom users table to Supabase's auth.users table
ALTER TABLE public.users 
ADD COLUMN IF NOT EXISTS auth_user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE;

-- Step 2: Create unique index on auth_user_id (optional but recommended for performance)
CREATE UNIQUE INDEX IF NOT EXISTS users_auth_user_id_idx ON public.users(auth_user_id);

-- Step 3: Drop the old password_hash column if it still exists
-- (User mentioned they already removed it, but this ensures it's gone)
ALTER TABLE public.users 
DROP COLUMN IF EXISTS password_hash;

-- =====================================================
-- AUTOMATIC USER METADATA POPULATION
-- =====================================================

-- Step 4: Create function to automatically create public.users entry when auth.users is created
-- This function will be triggered whenever a new user signs up via Supabase Auth
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = public
AS $$
BEGIN
  -- Insert into public.users with data from auth.users and user_metadata
  INSERT INTO public.users (
    id, 
    auth_user_id, 
    email, 
    full_name, 
    role, 
    shop_id,
    created_at
  )
  VALUES (
    gen_random_uuid(),                                    -- Generate new UUID for public.users.id
    NEW.id,                                               -- Link to auth.users.id
    NEW.email,                                            -- Email from auth.users
    COALESCE(NEW.raw_user_meta_data->>'full_name', ''),  -- Extract full_name from metadata
    COALESCE(NEW.raw_user_meta_data->>'role', 'client'), -- Extract role, default to 'client'
    CASE 
      WHEN NEW.raw_user_meta_data->>'shop_id' IS NOT NULL 
      THEN (NEW.raw_user_meta_data->>'shop_id')::UUID 
      ELSE NULL 
    END,                                                  -- Extract shop_id if present
    NOW()
  );
  
  RETURN NEW;
END;
$$;

-- Step 5: Create trigger that fires after a new user is inserted into auth.users
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;

CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW 
  EXECUTE FUNCTION public.handle_new_user();

-- =====================================================
-- ROW LEVEL SECURITY (RLS) POLICIES
-- =====================================================

-- Step 6: Enable RLS on public.users table (if not already enabled)
ALTER TABLE public.users ENABLE ROW LEVEL SECURITY;

-- Step 7: Drop existing policies if they exist (to avoid conflicts)
DROP POLICY IF EXISTS "Users can view their own data" ON public.users;
DROP POLICY IF EXISTS "Users can update their own data" ON public.users;
DROP POLICY IF EXISTS "Service role can do anything" ON public.users;
DROP POLICY IF EXISTS "Admins can view all users" ON public.users;

-- Step 8: Create RLS policies

-- Policy: Users can view their own user record
CREATE POLICY "Users can view their own data" 
ON public.users 
FOR SELECT 
USING (auth.uid() = auth_user_id);

-- Policy: Users can update their own user record
CREATE POLICY "Users can update their own data" 
ON public.users 
FOR UPDATE 
USING (auth.uid() = auth_user_id);

-- Policy: Service role (backend) can do anything
CREATE POLICY "Service role can do anything" 
ON public.users 
FOR ALL 
USING (auth.jwt() ->> 'role' = 'service_role');

-- Policy: Admin users can view all users
CREATE POLICY "Admins can view all users" 
ON public.users 
FOR SELECT 
USING (
  EXISTS (
    SELECT 1 FROM public.users 
    WHERE auth_user_id = auth.uid() 
    AND role = 'admin'
  )
);

-- =====================================================
-- UTILITY FUNCTIONS (Optional)
-- =====================================================

-- Function to get current user's role from public.users
CREATE OR REPLACE FUNCTION public.get_user_role(user_id UUID)
RETURNS TEXT
LANGUAGE sql
SECURITY DEFINER
AS $$
  SELECT role FROM public.users WHERE auth_user_id = user_id LIMIT 1;
$$;

-- Function to check if current user is admin
CREATE OR REPLACE FUNCTION public.is_admin()
RETURNS BOOLEAN
LANGUAGE sql
SECURITY DEFINER
AS $$
  SELECT EXISTS (
    SELECT 1 FROM public.users 
    WHERE auth_user_id = auth.uid() 
    AND role = 'admin'
  );
$$;

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Run these to verify the migration was successful:

-- Check if auth_user_id column exists
-- SELECT column_name, data_type 
-- FROM information_schema.columns 
-- WHERE table_name = 'users' AND table_schema = 'public';

-- Check if trigger exists
-- SELECT trigger_name, event_manipulation, event_object_table 
-- FROM information_schema.triggers 
-- WHERE trigger_name = 'on_auth_user_created';

-- Check RLS policies
-- SELECT schemaname, tablename, policyname, permissive, roles, cmd, qual 
-- FROM pg_policies 
-- WHERE tablename = 'users';

-- =====================================================
-- MIGRATION COMPLETE
-- =====================================================
-- After running this script:
-- 1. Update your supabase.properties with the anon key
-- 2. Test user registration through your JavaFX app
-- 3. Verify that entries appear in both auth.users and public.users
-- =====================================================

