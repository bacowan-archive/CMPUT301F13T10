/*
Copyright (c) 2013, Brendan Cowan, Tyler Meen, Steven Gerdes, Braeden Soetaert, Aly-khan Jamal
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
 */
package cmput301f13t10.presenter;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import cmput301f13t10.model.AdventureCache;
import cmput301f13t10.model.AdventureModel;
import cmput301f13t10.model.SectionModel;
import cmput301f13t10.view.SectionEditView;
import cmput301f13t10.view.UpdatableView;

/**
 * Presenter for the Adventure group of classes, as per the MVP pattern.
 * 
 * @author Brendan Cowan
 * @author Steven Gerdes
 * 
 */
public class SectionPresenter
{

	/**
	 * The view that created the presenter
	 */
	private UpdatableView mView;
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
	public SectionPresenter( UpdatableView view )
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
		mCurrentAdventure.setSection( mCurrentSection );
		mView.updateView();
	}

	/**
	 * Set the current section that the user is viewing by the sections id
	 * 
	 * @param sectionId
	 *            The id of the section to set
	 */
	public void setCurrentSectionById( Integer sectionId )
	{
		if( sectionId == null )
			setCurrentSection( new SectionModel( "" ) );
		else
			setCurrentSection( mCurrentAdventure.getSection( sectionId ) );
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
		setCurrentSection( mCurrentAdventure.getCurrentSection() );
	}

	/**
	 * Get the possible choices for the current section
	 * 
	 * @return An ArrayList of strings indicating possible choices
	 */
	public ArrayList<SectionChoice> getChoices()
	{// TODO: change this hack to something nicer
		ArrayList<SectionChoice> choices = mCurrentSection.getChoices();
		ArrayList<SectionModel> sections = mCurrentAdventure.getSections();
		for( SectionChoice choice : choices )
		{
			boolean sectionExists = false;
			for( SectionModel section : sections )
			{
				if( choice.getSectionTitle().getId() == section.getId() )
				{
					choice.getSectionTitle().setTitle( section.getName() );
					sectionExists = true;
				}
			}
			if( !sectionExists )
				choices.remove( choice );
		}
		return choices;
	}

	/**
	 * Get the title of the current section
	 * 
	 * @return The title of the current section
	 */
	public ArrayList<SectionTitle> getSectionTitles()
	{
		ArrayList<SectionModel> sectionChoices = mCurrentAdventure.getSections();
		ArrayList<SectionTitle> sectionTitles = new ArrayList<SectionTitle>();

		for( SectionModel s : sectionChoices )
			sectionTitles.add( new SectionTitle( s.getName(), s.getId() ) );

		return sectionTitles;
	}

	public String getSectionTitle()
	{
		return mCurrentSection.getName();
	}

	/**
	 * Set set the title of the current section, and update the view to conform
	 * with the change.
	 * 
	 * @param sectionName
	 *            The new name of the section
	 */
	public void UpdateSectionTitle( String sectionName )
	{
		mCurrentSection.setName( sectionName );
		mView.updateView();
	}

	/**
	 * Get the id of the current adventure
	 * 
	 * @return The id of the current adventure
	 */
	public int getAdventureId()
	{
		return mCurrentAdventure.getLocalId();
	}

	/**
	 * Get the id of the current section
	 * 
	 * @return The id of the current section
	 */
	public int getSectionId()
	{
		return mCurrentSection.getId();
	}

	/**
	 * Takes a bitmap (which is store in the intent) and adds it as a Media to
	 * the current section.
	 * 
	 * @param editView
	 *            The current SectionEditView
	 * @param data
	 *            The data taken from the camera
	 */
	public void storeImage( SectionEditView view, Intent data )
	{
		ImageMedia newImageMedia = ImageCreator.storeImage( view, data );
		if( newImageMedia != null )
		{
			mCurrentSection.add( newImageMedia );
		}
	}

	/**
	 * This takes a new bitmap and the position of the media in the current
	 * sections media list and sets the new bitmap
	 * 
	 * @param newBitmap
	 *            New bitmap to get set
	 * @param mediaPos
	 *            Position in media array list
	 */
	public void resizeBitmap( Bitmap newBitmap, int mediaPos )
	{
		ImageCreator.resizeBitmap( newBitmap, mediaPos, mCurrentSection.getMedia() );
	}

	public ArrayList<Media> getMedia()
	{
		return mCurrentSection.getMedia();
	}

	/**
	 * 
	 * @return if this is the last section in the chain
	 */
	public boolean atLastSection()
	{
		return mCurrentSection.getChoices().isEmpty();
	}

	/**
	 * This takes the separate information from the view and makes it into a
	 * choice
	 * 
	 * @param id
	 *            the id of the choice to add
	 * @param decisionText
	 *            the decision the user has to make
	 * @param sectionTitle
	 *            the title of the section
	 */
	public void addSectionChoice( Integer id, String decisionText, String sectionTitle )
	{
		SectionModel section = mCurrentAdventure.getSection( id );
		if( section == null )
		{
			section = new SectionModel( sectionTitle );
			mCurrentAdventure.addSection( section );
		}

		mCurrentSection.addChoice( new SectionChoice( new SectionTitle( section.getName(), section.getId() ), decisionText ) );
		mView.updateView();
	}

	/**
	 * @return just the descriptions of each choice
	 */
	public ArrayList<String> getChoiceDescriptions()
	{
		ArrayList<SectionChoice> sectionChoices = mCurrentSection.getChoices();
		ArrayList<String> stringChoices = new ArrayList<String>();
		for( SectionChoice s : sectionChoices )
		{
			stringChoices.add( s.getChoiceDescription() );
		}
		return stringChoices;
	}

	/**
	 * removes the section choice from the current section
	 * 
	 * @param choiceToRemove
	 *            the choice to remove
	 */
	public void removeSectionChoice( SectionChoice choiceToRemove )
	{
		mCurrentSection.removeChoice( choiceToRemove );
		mView.updateView();
	}

	public void setNextSectionByIndex( int index )
	{
		if( mCurrentSection == null )
			return;
		setCurrentSection( mCurrentAdventure.getSection( mCurrentSection.getChoices().get( index ).getSectionTitle().getId() ) );
	}

	public void setRandomAdventure()
	{
		Random rand = new Random();
		setNextSectionByIndex( rand.nextInt( mCurrentSection.getChoices().size() ) );
	}

	public boolean isRandomSet()
	{
		return mCurrentAdventure.getRandomSet();
	}
}
