package cmput301f13t10;

import java.util.ArrayList;
import java.util.Observable;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Presenter for the Adventure group of classes, as per the MVP pattern.
 * 
 * @author Brendan Cowan
 * 
 */
public class SectionPresenter implements Presenter
{

	/**
	 * The view that created the presenter
	 */
	private SectionView mView;
	/**
	 * The current section that the reader is viewing
	 */
	private SectionModel mCurrentSection;
	/**
	 * The current adventure that the reader is viewing
	 */
	private AdventureModel mCurrentAdventure;

	/**
	 * Constructor
	 * 
	 * @param view
	 *            The view that created this presenter
	 */
	public SectionPresenter( SectionView view )
	{
		mView = view;
	}

	/**
	 * Set the current section that the user is viewing
	 * 
	 * @param section
	 *            The current section to set
	 */
	public void setCurrentSection( SectionModel section )
	{
		mCurrentSection = section;
		mView.updateAdventureSection();
	}

	/**
	 * Set the current section that the user is viewing by the sections id
	 * 
	 * @param sectionId
	 *            The id of the section to set
	 */
	public void setCurrentSectionId( int sectionId )
	{
		setCurrentSection( mCurrentSection.getChoiceSection( sectionId ) );
	}

	/**
	 * Set the input view group to contain all media in the current section.
	 * 
	 * @param vg
	 *            The view group that is to contain the media.
	 */
	public void setCurrentSectionView( ViewGroup vg )
	{
		try
		{
			ArrayList<Media> medias = mCurrentSection.getMedia();
			vg.removeAllViews();
			for( Media m : medias )
			{
				vg.addView( m.toView( mView ) );
			}
		}
		catch( NullPointerException e )
		{
			Logger.log( "No current section", e );
		}
	}

	/**
	 * Set the current Adventure that the user is viewing
	 * 
	 * @param adventure
	 *            The current adventure to set
	 */
	public void setCurrentAdventure( int adventure )
	{
		mCurrentAdventure = AdventureCache.getAdventureCache().getAdventureById( adventure );
		setCurrentSection( mCurrentAdventure.getStartSection() );
	}

	/**
	 * Get the possible choices for the current section
	 * 
	 * @return An ArrayList of strings indicating possible choices
	 */
	public ArrayList<String> getChoices()
	{
		ArrayList<String> stringChoices = null;
		try
		{
			ArrayList<SectionChoice> sectionChoices = mCurrentSection.getChoices();
			stringChoices = new ArrayList<String>();
			for( SectionChoice s : sectionChoices )
			{
				stringChoices.add( s.getUserChoice() );
			}
		}
		catch( NullPointerException e )
		{
			Logger.log( "No current section", e );
		}
		return stringChoices;
	}

	@Override
	public void update( Observable arg0, Object arg1 )
	{

	}

}