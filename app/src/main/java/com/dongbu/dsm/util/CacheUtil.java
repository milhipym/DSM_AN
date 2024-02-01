package com.dongbu.dsm.util;

/**
 * Created by 81700905 on 2017-09-06.
 */

import android.content.Context;
import android.util.Log;

import com.dongbu.dsm.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheUtil {

    private static Context mContext;
    private static File cacheDir;

    // This doesn't need to be a singleton for most cases, however if two locations of the app
    // are reading and writing to the same location it may be better to be one as to not create
    // any errors with the saved data
    public static CacheUtil getInstance( Context context )
    {
        if( mContext == null || mContext != context )
        {
            mContext = context;
            cacheDir = mContext.getCacheDir();
        }

        if( cache == null ) cache = new CacheUtil( context );
        return cache;
    }

    private static CacheUtil cache;
    private CacheUtil( Context context )
    {
        // singleton
        cacheDir = mContext.getCacheDir();
    }

    /* Commonly used operations **/

    public boolean clearCache()
    {
        return clearCache(cacheDir);
    }

    private boolean clearCache( File file )
    {
        if( file.isDirectory() )
        {
            File[] children = file.listFiles();

            // delete recursive children first
            for( File child : children )
            {
                clearCache(child);
            }

            // delete parent
            return file.delete();
        }
        else
        {
            // delete regular file
            return file.delete();
        }
    }

    public boolean contains( String name )
    {
        return contains( cacheDir, name );
    }

    private boolean contains( File file, String name )
    {
        if( file.isDirectory() )
        {
            File[] children = file.listFiles();
            for( File child : children )
            {
                if( contains( child, name ) )
                {
                    return true;
                }
            }
        }

        return file.getName().equalsIgnoreCase( name );
    }

    public boolean delete( String name )
    {
        return clearCache( new File( cacheDir, name ) );
    }

    public String read( String name )
    {
        return read( new File( cacheDir, name ) );
    }

    public String read( File file )
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream( file );

            byte[] byteData = new byte[fis.available()];
            fis.read( byteData );

            return new String( byteData );
        } catch (FileNotFoundException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (IOException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        finally
        {
            try
            {
                if( fis != null )
                    fis.close();
            }
            catch( IOException e )
            {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
        }
        return null;
    }

    public boolean save( String name, String data )
    {
        return save(new File(cacheDir, name), data);
    }

    public boolean save( File file, String data )
    {
        try
        {
            // if file wasn't created yet, make one
            if( !file.exists() )
            {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream( file );
            byte[] byteData = data.getBytes();

            // write data
            fos.write( byteData );

            // immediately clear buffer onto file
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (IOException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return false;
    }


    /** DEBUGGING USE **/
    public void dump()
    {
        dump( cacheDir );
    }

    private void dump( File file )
    {
        if( file.isDirectory() )
        {
            Log.d( "Directory", file.getAbsolutePath() );
            File[] children = file.listFiles();

            // dump date for children
            for( File child : children )
            {
                dump( child );
            }
        }
        else
        {
            Log.d( "File", file.getAbsolutePath() );
        }
    }

}


