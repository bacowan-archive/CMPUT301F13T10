package cmput301f13t10;

import java.util.HashMap;
import java.util.Map;

/**
 * A storage of adventures. Whoever is writing this class should make this better.
 * @author 
 *
 */
public class AdventureCache implements AdventureInteractor
{
	private static AdventureCache ac = null;
	private Map<Integer, AdventureModel> adventures;

	protected AdventureCache()
	{
		adventures = new HashMap<Integer, AdventureModel>();
	}

	public static AdventureCache getAdventureCache()
	{
		if( ac == null )
			ac = new AdventureCache();
		return ac;
	}

	@Override 
	public void addAdventure( AdventureModel adventure )
	{
		adventures.put( adventure.getId(), adventure );
	}

	@Override
	public AdventureModel getAdventureById( int id )
	{
		return adventures.get( id );
	}
}
