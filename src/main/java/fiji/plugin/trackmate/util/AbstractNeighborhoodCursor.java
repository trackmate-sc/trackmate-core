package fiji.plugin.trackmate.util;

import net.imglib2.Cursor;
import net.imglib2.outofbounds.Bounded;
import net.imglib2.outofbounds.OutOfBounds;

public abstract class AbstractNeighborhoodCursor< T > implements Cursor< T >,
		Bounded
{

	protected AbstractNeighborhood< T > neighborhood;

	protected final OutOfBounds< T > ra;

	/*
	 * CONSTRUCTOR
	 */

	public AbstractNeighborhoodCursor( final AbstractNeighborhood< T > neighborhood )
	{
		this.neighborhood = neighborhood;
		this.ra = neighborhood.extendedSource.randomAccess();
	}

	/*
	 * METHODS
	 */

	@Override
	public void localize( final float[] position )
	{
		ra.localize( position );
	}

	@Override
	public void localize( final double[] position )
	{
		ra.localize( position );
	}

	@Override
	public float getFloatPosition( final int d )
	{
		return ra.getFloatPosition( d );
	}

	@Override
	public double getDoublePosition( final int d )
	{
		return ra.getDoublePosition( d );
	}

	@Override
	public int numDimensions()
	{
		return ra.numDimensions();
	}

	@Override
	public T get()
	{
		return ra.get();
	}

	/**
	 * This dummy method just calls {@link #fwd()} multiple times.
	 */
	@Override
	public void jumpFwd( final long steps )
	{
		for ( int i = 0; i < steps; i++ )
		{
			fwd();
		}
	}

	@Override
	public T next()
	{
		fwd();
		return ra.get();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException( "remove() is not implemented for " + getClass().getCanonicalName() );
	}

	@Override
	public void localize( final int[] position )
	{
		ra.localize( position );
	}

	@Override
	public void localize( final long[] position )
	{
		ra.localize( position );
	}

	@Override
	public int getIntPosition( final int d )
	{
		return ra.getIntPosition( d );
	}

	@Override
	public long getLongPosition( final int d )
	{
		return ra.getLongPosition( d );
	}

	@Override
	public boolean isOutOfBounds()
	{
		return ra.isOutOfBounds();
	}

}