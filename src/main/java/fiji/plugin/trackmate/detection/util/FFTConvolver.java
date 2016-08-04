package fiji.plugin.trackmate.detection.util;

import fiji.plugin.trackmate.detection.DetectionUtils;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.MultiThreaded;
import net.imglib2.algorithm.fft2.FFT;
import net.imglib2.algorithm.fft2.FFTMethods;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.complex.ComplexFloatType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

public class FFTConvolver implements MultiThreaded
{

	private int nThreads = 1;

	private final Img< ComplexFloatType > kernelfft;

	private final Interval paddingIntervalCentered;

	public FFTConvolver( final RandomAccessibleInterval< FloatType > kernel, final Interval interval )
	{
		setNumThreads();
		
		final long[] paddedDimensions = new long[ interval.numDimensions() ];
		final long[] fftDimensions = new long[ interval.numDimensions() ];
		FFTMethods.dimensionsRealToComplexFast( interval, paddedDimensions, fftDimensions );
		final Dimensions paddingDimensions = FinalDimensions.wrap( paddedDimensions );
		this.paddingIntervalCentered = FFTMethods.paddingIntervalCentered( interval, paddingDimensions );

		final Interval kernelPaddingIntervalCentered = FFTMethods.paddingIntervalCentered( kernel, paddingDimensions );

		final long[] min = new long[ interval.numDimensions() ];
		final long[] max = new long[ interval.numDimensions() ];

		for ( int d = 0; d < interval.numDimensions(); ++d )
		{
			min[ d ] = kernel.min( d ) + kernel.dimension( d ) / 2;
			max[ d ] = min[ d ] + kernelPaddingIntervalCentered.dimension( d ) - 1;
		}

		// Forward FFT
		final RandomAccessibleInterval< FloatType > kernelRAI = Views.interval(
				Views.extendPeriodic(
						Views.interval(
								Views.extendZero( kernel ),
								kernelPaddingIntervalCentered ) ),
				new FinalInterval( min, max ) );

		// Factory
		final ImgFactory< ComplexFloatType > factory = Util.getArrayOrCellImgFactory( kernelRAI, new ComplexFloatType() );

		// Forward FFT of kernel.
		this.kernelfft = FFT.realToComplex( kernelRAI, factory, nThreads );

	}

	public < T extends RealType< T > > void convolve( final RandomAccessibleInterval< T > source, final RandomAccessibleInterval< FloatType > output )
	{
		// Forward FFT
		final RandomAccessibleInterval< T > imgRAI = Views.interval( 
				Views.extendPeriodic( source ),
				paddingIntervalCentered );

		// Factory
		final ImgFactory< ComplexFloatType > factory = Util.getArrayOrCellImgFactory( imgRAI, new ComplexFloatType() );

		// Forward FFT of source.
		final Img< ComplexFloatType > fft = FFT.realToComplex( imgRAI, factory, nThreads );

		// Complex multiplication.
		final Cursor< ComplexFloatType > cursor = fft.localizingCursor();
		final RandomAccess< ComplexFloatType > ra = Views.extendZero( kernelfft ).randomAccess( fft );
		while ( cursor.hasNext() )
		{
			cursor.fwd();
			ra.setPosition( cursor );
			cursor.get().mul( ra.get() );
		}

		// Output
		FFT.complexToRealUnpad( fft, output, nThreads );
	}

	/*
	 * MULTITHREADED.
	 */

	@Override
	public int getNumThreads()
	{
		return nThreads;
	}

	@Override
	public void setNumThreads()
	{
		setNumThreads( Runtime.getRuntime().availableProcessors() );
	}

	@Override
	public void setNumThreads( final int nThreads )
	{
		this.nThreads = nThreads;
	}

	/*
	 * MAIN METHOD.
	 */

	public static void main( final String[] args )
	{
		ImageJ.main( args );
		final ImageProcessor ip = IJ.openImage( "samples/FakeTracks.tif" ).getStack().getProcessor( 20 );

		final Img< UnsignedByteType > source = ImageJFunctions.wrapByte( new ImagePlus( "Source", ip ) );

		final Interval interval = FinalInterval.createMinSize( 10, 28, 79, 47 );
		final Img< FloatType > kernel = DetectionUtils.createLoGKernel( 2.5, 2, new double[] { 1., 1. } );
		final FFTConvolver convolver = new FFTConvolver( kernel, interval );

		final ArrayImg< FloatType, FloatArray > output = ArrayImgs.floats(
				interval.dimension( 0 ),
				interval.dimension( 1 ) );

		convolver.convolve( source, output );

		ImageJFunctions.show( output, "Output" );

	}
}
