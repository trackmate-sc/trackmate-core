package fiji.plugin.trackmate.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;

public final class GuiConstants
{
	public static final Font FONT = new Font( "Arial", Font.PLAIN, 10 );

	public static final Font BIG_FONT = new Font( "Arial", Font.PLAIN, 14 );

	public static final Font SMALL_FONT = FONT.deriveFont( 8 );

	public static final Dimension TEXTFIELD_DIMENSION = new Dimension( 40, 18 );

	public static final ImageIcon TRACKMATE_ICON = new ImageIcon( GuiConstants.class.getResource( "Logo50x50-color-nofont-72p.png" ) );

	public static final ImageIcon ICON_REFRESH = new ImageIcon( GuiConstants.class.getResource( "arrow_refresh_small.png" ) );

	public static final ImageIcon ICON_PREVIEW = new ImageIcon( GuiConstants.class.getResource( "flag_checked.png" ) );

	public static final ImageIcon ICON_ADD = new ImageIcon( GuiConstants.class.getResource( "add.png" ) );

	public static final ImageIcon ICON_REMOVE = new ImageIcon( GuiConstants.class.getResource( "delete.png" ) );

	private GuiConstants()
	{}

}
