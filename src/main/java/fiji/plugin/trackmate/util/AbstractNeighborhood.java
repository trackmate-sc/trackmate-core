package fiji.plugin.trackmate.util;

import net.imglib2.IterableInterval;
import net.imglib2.Localizable;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPositionable;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

/**
 * A {@link Positionable} {@link IterableInterval} that serves as a local
 * neighborhood, e.g. in filtering operation.
 * <p>
 * This particular class implements a movable nD domain, defined by a
 * <code>span long[]</code> array. The <code>span</code> array is such that the
 * bounding box of the neighborhood in dimension <code>d</code> will be
 * <code>2 x span[d] + 1</code>.
 */
public abstract class AbstractNeighborhood< T >
		implements Positionable, IterableInterval< T >
{

	/** The pixel coordinates of the center of this regions. */
	protected final long[] center;

	/**
	 * The span of this neighborhood, such that the size of the bounding box in
	 * dimension <code>d</code> will be <code>2 x span[d] + 1</code>.
	 */
	protected final long[] span;

	protected ExtendedRandomAccessibleInterval< T, RandomAccessibleInterval< T > > extendedSource;

	protected RandomAccessibleInterval< T > source;

	protected OutOfBoundsFactory< T, RandomAccessibleInterval< T > > outOfBounds;

	protected int n;

	/*
	 * CONSTRUCTOR
	 */

	public AbstractNeighborhood( final int numDims,
			final OutOfBoundsFactory< T, RandomAccessibleInterval< T > > outOfBounds )
	{
		this.n = numDims;
		this.outOfBounds = outOfBounds;
		this.center = new long[ numDims ];
		this.span = new long[ numDims ];
	}

	/*
	 * METHODS
	 */

	/**
	 * Set the span of this neighborhood.
	 * <p>
	 * The neighborhood span is such that the size of the neighborhood in
	 * dimension <code>d</code> will be <code>2 x span[d] + 1</code>.
	 *
	 * @param span
	 *            this array content will be copied to the neighborhood internal
	 *            field.
	 */
	public void setSpan( final long[] span )
	{
		for ( int d = 0; d < span.length; d++ )
		{
			this.span[ d ] = span[ d ];
		}
	}

	@Override
	public int numDimensions()
	{
		return n;
	}

	@Override
	public void fwd( final int d )
	{
		center[ d ]++;
	}

	@Override
	public void bck( final int d )
	{
		center[ d ]--;
	}

	@Override
	public void move( final int distance, final int d )
	{
		center[ d ] = center[ d ] + distance;
	}

	@Override
	public void move( final long distance, final int d )
	{
		center[ d ] = center[ d ] + distance;
	}

	@Override
	public void move( final Localizable localizable )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			center[ d ] += localizable.getLongPosition( d );
		}
	}

	@Override
	public void move( final int[] distance )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			center[ d ] += distance[ d ];
		}
	}

	@Override
	public void move( final long[] distance )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			center[ d ] += distance[ d ];
		}
	}

	@Override
	public void setPosition( final Localizable localizable )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			center[ d ] = localizable.getLongPosition( d );
		}
	}

	@Override
	public void setPosition( final int[] position )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			center[ d ] = position[ d ];
		}
	}

	@Override
	public void setPosition( final long[] position )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			center[ d ] = position[ d ];
		}
	}

	@Override
	public void setPosition( final int position, final int d )
	{
		center[ d ] = position;
	}

	@Override
	public void setPosition( final long position, final int d )
	{
		center[ d ] = position;
	}

	/**
	 * Return the element at the top-left corner of this nD neighborhood.
	 */
	@Override
	public T firstElement()
	{
		final RandomAccess< T > ra = source.randomAccess();
		for ( int d = 0; d < span.length; d++ )
		{
			ra.setPosition( center[ d ] - span[ d ], d );
		}
		return ra.get();
	}

	@Override
	public Object iterationOrder()
	{
		return this;
	}

	@Override
	public double realMin( final int d )
	{
		return center[ d ] - span[ d ];
	}

	@Override
	public void realMin( final double[] min )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			min[ d ] = center[ d ] - span[ d ];
		}

	}

	@Override
	public void realMin( final RealPositionable min )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			min.setPosition( center[ d ] - span[ d ], d );
		}
	}

	@Override
	public double realMax( final int d )
	{
		return center[ d ] + span[ d ];
	}

	@Override
	public void realMax( final double[] max )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			max[ d ] = center[ d ] + span[ d ];
		}
	}

	@Override
	public void realMax( final RealPositionable max )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			max.setPosition( center[ d ] + span[ d ], d );
		}
	}

	@Override
	public long min( final int d )
	{
		return center[ d ] - span[ d ];
	}

	@Override
	public void min( final long[] min )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			min[ d ] = center[ d ] - span[ d ];
		}
	}

	@Override
	public void min( final Positionable min )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			min.setPosition( center[ d ] - span[ d ], d );
		}
	}

	@Override
	public long max( final int d )
	{
		return center[ d ] + span[ d ];
	}

	@Override
	public void max( final long[] max )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			max[ d ] = center[ d ] + span[ d ];
		}
	}

	@Override
	public void max( final Positionable max )
	{
		for ( int d = 0; d < center.length; d++ )
		{
			max.setPosition( center[ d ] + span[ d ], d );
		}
	}

	@Override
	public void dimensions( final long[] dimensions )
	{
		for ( int d = 0; d < span.length; d++ )
		{
			dimensions[ d ] = 2 * span[ d ] + 1;
		}
	}

	@Override
	public long dimension( final int d )
	{
		return ( 2 * span[ d ] + 1 );
	}

	/**
	 * Updates the source.
	 */
	public void updateSource( final RandomAccessibleInterval< T > source )
	{
		this.source = source;
		this.extendedSource = Views.extend( source, outOfBounds );
	}

	/**
	 * Copies the {@link AbstractNeighborhood}.
	 */
	public abstract AbstractNeighborhood< T > copy();

}