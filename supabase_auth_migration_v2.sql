-- =====================================================
-- SUPABASE AUTH MIGRATION SCRIPT V2 (FIXED)
-- This version fixes the trigger issue
-- =====================================================
-- Run this in your Supabase SQL Editor

-- Step 1: Check current users table schema
SELECT column_name, data_type, is_nullable
FROM information_schema.columns 
WHERE table_name = 'users' AND table_schema = 'public'
ORDER BY ordinal_position;

-- Step 2: Drop the old trigger if it exists
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
DROP FUNCTION IF EXISTS public.handle_new_user();

-- Step 3: Ensure auth_user_id column exists and is properly configured
DO $$ 
BEGIN
    -- Check if auth_user_id column exists
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'public' 
        AND table_name = 'users' 
        AND column_name = 'auth_user_id'
    ) THEN
        -- Add the column
        ALTER TABLE public.users ADD COLUMN auth_user_id UUID;
    END IF;
END $$;

-- Create unique index
DROP INDEX IF EXISTS users_auth_user_id_idx;
CREATE UNIQUE INDEX users_auth_user_id_idx ON public.users(auth_user_id);

-- Add foreign key constraint if not exists
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE constraint_name = 'users_auth_user_id_fkey'
    ) THEN
        ALTER TABLE public.users 
        ADD CONSTRAINT users_auth_user_id_fkey 
        FOREIGN KEY (auth_user_id) 
        REFERENCES auth.users(id) 
        ON DELETE CASCADE;
    END IF;
END $$;

-- Step 4: Create improved trigger function with better error handling
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = public
AS $$
DECLARE
    user_full_name TEXT;
    user_role TEXT;
    user_shop_id UUID;
BEGIN
    -- Extract metadata with safe defaults
    user_full_name := COALESCE(NEW.raw_user_meta_data->>'full_name', '');
    user_role := COALESCE(NEW.raw_user_meta_data->>'role', 'client');
    
    -- Handle shop_id (can be NULL)
    BEGIN
        user_shop_id := (NEW.raw_user_meta_data->>'shop_id')::UUID;
    EXCEPTION WHEN OTHERS THEN
        user_shop_id := NULL;
    END;
    
    -- Insert into public.users
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
        gen_random_uuid(),
        NEW.id,
        NEW.email,
        user_full_name,
        user_role,
        user_shop_id,
        NOW()
    );
    
    RETURN NEW;
EXCEPTION
    WHEN OTHERS THEN
        -- Log error but don't fail the auth user creation
        RAISE WARNING 'Error in handle_new_user trigger: %', SQLERRM;
        RETURN NEW;
END;
$$;

-- Step 5: Create the trigger
CREATE TRIGGER on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW
    EXECUTE FUNCTION public.handle_new_user();

-- Step 6: Grant necessary permissions
GRANT USAGE ON SCHEMA public TO postgres, anon, authenticated, service_role;
GRANT ALL ON public.users TO postgres, anon, authenticated, service_role;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO postgres, anon, authenticated, service_role;

-- Step 7: Verify the trigger was created
SELECT 
    trigger_name, 
    event_object_table, 
    action_statement,
    action_timing,
    event_manipulation
FROM information_schema.triggers 
WHERE trigger_name = 'on_auth_user_created';

-- =====================================================
-- CLEANUP: Remove any failed user registrations
-- =====================================================
-- If you have users in auth.users but not in public.users, this will clean them up
-- UNCOMMENT THE FOLLOWING LINES IF YOU WANT TO CLEAN UP:

-- DELETE FROM auth.users 
-- WHERE id NOT IN (SELECT auth_user_id FROM public.users WHERE auth_user_id IS NOT NULL);

-- =====================================================
-- TEST THE TRIGGER
-- =====================================================
-- You can test the trigger is working by checking:
-- 1. Try to register a user in your app
-- 2. Check auth.users table - user should be created
-- 3. Check public.users table - user should also be created with auth_user_id linking to auth.users.id

SELECT 'Migration complete! Trigger is ready.' as status;

