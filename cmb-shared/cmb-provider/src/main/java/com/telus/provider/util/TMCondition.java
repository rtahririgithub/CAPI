package com.telus.provider.util;

import java.util.*;
import java.io.*;

/**
 * <code>Condition</code>
 *
 *  This class was borrowed, with permission, from the AbstractNotions SDK.
 *
 * @author    Dele Taylor
 * @version  $Revision: 1.3 $, $Date: 2001/09/28 01:20:50 $
 */
public final class TMCondition implements Serializable
{

    private boolean _true;


    public TMCondition()
    {
    }

    public TMCondition(boolean _true)
    {
        this._true = _true;
    }
    public boolean isTrue()
    {
        return _true;
    }

    public void setTrue()
    {
        set(true);
    }

    public void setFalse()
    {
        set(false);
    }

    public synchronized void set(boolean _true)
    {
        if(this._true != _true)
        {
            this._true = _true;
            notifyAll();
        }
    }

    public synchronized void waitForTrue(long timeout) throws InterruptedException
    {
        long finish = System.currentTimeMillis() + timeout;
        while(!_true && timeout > 0)
        {
            wait(timeout);
            timeout = (finish - System.currentTimeMillis());
        }

        if(!_true)
        {
            throw new InterruptedException("timed-out");
        }
    }

    public synchronized void waitForTrue() throws InterruptedException
    {
        while(!_true)
        {
            wait();
        }
    }

    public synchronized void waitForFalse(long timeout) throws InterruptedException
    {
        long finish = System.currentTimeMillis() + timeout;
        while(_true && timeout > 0)
        {
            wait(timeout);
            timeout = (finish - System.currentTimeMillis());
        }

        if(_true)
        {
            throw new InterruptedException("timed-out");
        }
    }

    public synchronized void waitForFalse() throws InterruptedException
    {
        while(_true)
        {
            wait();
        }
    }


/*
    public synchronized void releaseAll()
    {
        notifyAll();
    }

    public synchronized void releaseOne()
    {
        notify();
    }
*/
}
