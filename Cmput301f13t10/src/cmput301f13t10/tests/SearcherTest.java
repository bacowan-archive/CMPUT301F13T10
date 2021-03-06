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

package cmput301f13t10.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cmput301f13t10.model.AdventureModel;
import cmput301f13t10.model.InvalidSearchTypeException;
import cmput301f13t10.presenter.Searcher;

/**
 * SearcherTest
 * 
 * Version 0.1
 * 
 * October 18, 2013
 * 
 * Test the searcher
 * 
 * @author Brendan Cowan
 */
public class SearcherTest
{

	private ArrayList<AdventureModel> mAdventures;
	private AdventureModel mFullMatch;
	private AdventureModel mPartMatch;
	private AdventureModel mNoMatch;
	private List<AdventureModel> mResults;
	private Exception mThrown;

	public SearcherTest()
	{
	}

	@Before
	public void setUp() throws Exception
	{
		mAdventures = new ArrayList<AdventureModel>();

		mFullMatch = new AdventureModel( "FullMatch" );
		mPartMatch = new AdventureModel( "PartMatch" );
		mNoMatch = new AdventureModel( "NoMatch" );

		mFullMatch.setTitle( "bcd" );
		mPartMatch.setTitle( "abcdef" );
		mNoMatch.setTitle( "cd" );

		mAdventures.add( mFullMatch );
		mAdventures.add( mPartMatch );
		mAdventures.add( mNoMatch );
	}

	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * testSearchByTitle Can the searcher return a list of adventures matching a
	 * title pattern?
	 */
	@Test
	public void testSearchByTitle()
	{

		try
		{
			mResults = Searcher.searchBy( mAdventures, "bcd", Searcher.sTITLE );
		}
		catch( InvalidSearchTypeException e )
		{
			mThrown = e;
		}

		org.junit.Assert.assertNull( mThrown );
		assertEquals( mResults.size(), 3 );
		assertTrue( mResults.get( 0 ).equals( mFullMatch ) || mResults.get( 0 ).equals( mPartMatch ) );
		assertTrue( mResults.get( 1 ).equals( mFullMatch ) || mResults.get( 1 ).equals( mPartMatch ) );
		assertTrue( mResults.contains( mNoMatch ) );
	}

	/**
	 * When there is a blank search, the search results should come back in
	 * alphabetical order
	 */
	@Test
	public void testBlankSearch()
	{
		try
		{
			mResults = Searcher.searchBy( mAdventures, "", Searcher.sTITLE );
		}
		catch( InvalidSearchTypeException e )
		{
			mThrown = e;
		}

		org.junit.Assert.assertNull( mThrown );
		assertTrue( mResults.get( 0 ).equals( mPartMatch ) );
		assertTrue( mResults.get( 1 ).equals( mFullMatch ) );
		assertTrue( mResults.get( 2 ).equals( mNoMatch ) );

	}

	/**
	 * testBadSearchType Can the searcher give a valid error if the search type
	 * does not exist?
	 */
	@Test
	public void testBadSearchType()
	{
		try
		{
			mResults = Searcher.searchBy( mAdventures, "bcd", "invalid" );
		}
		catch( InvalidSearchTypeException e )
		{
			mThrown = e;
		}
		org.junit.Assert.assertNotNull( mThrown );
	}

	/**
	 * test that the searcher can handle single character searches.
	 */
	@Test
	public void testOneLetterNames()
	{
		ArrayList<AdventureModel> advs = new ArrayList<AdventureModel>();

		AdventureModel a = new AdventureModel( "a" );
		AdventureModel b = new AdventureModel( "b" );
		AdventureModel c = new AdventureModel( "c" );
		AdventureModel six = new AdventureModel( "6" );
		AdventureModel space = new AdventureModel( " " );

		List<AdventureModel> searchA = new ArrayList<AdventureModel>();
		List<AdventureModel> searchB = new ArrayList<AdventureModel>();
		List<AdventureModel> searchC = new ArrayList<AdventureModel>();
		List<AdventureModel> searchSix = new ArrayList<AdventureModel>();
		List<AdventureModel> searchSpace = new ArrayList<AdventureModel>();

		advs.add( a );
		advs.add( b );
		advs.add( c );
		advs.add( six );
		advs.add( space );

		try
		{
			searchA = Searcher.searchBy( advs, "a", Searcher.sTITLE );
			searchB = Searcher.searchBy( advs, "b", Searcher.sTITLE );
			searchC = Searcher.searchBy( advs, "c", Searcher.sTITLE );
			searchSix = Searcher.searchBy( advs, "6", Searcher.sTITLE );
			searchSpace = Searcher.searchBy( advs, " ", Searcher.sTITLE );
		}
		catch( InvalidSearchTypeException e )
		{
			mThrown = e;
		}

		org.junit.Assert.assertNull( mThrown );

		assertTrue( searchA.get( 0 ).equals( a ) );
		assertTrue( searchB.get( 0 ).equals( b ) );
		assertTrue( searchC.get( 0 ).equals( c ) );
		assertTrue( searchSix.get( 0 ).equals( six ) );
		assertTrue( searchSpace.get( 0 ).equals( space ) );

	}

}
