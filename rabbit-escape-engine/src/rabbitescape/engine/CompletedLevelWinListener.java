package rabbitescape.engine;

import rabbitescape.engine.menu.LevelsCompleted;

public class CompletedLevelWinListener implements LevelWinListener
{
    private final String levelsDir;
    private final int levelNumber;
    private final LevelsCompleted levelsCompleted;
   // private final World world;

    public CompletedLevelWinListener(
        String levelsDir,
        int levelNumber,
        LevelsCompleted levelsCompleted
       // ,World world
    )
    {
        this.levelsDir = levelsDir;
        this.levelNumber = levelNumber;
        this.levelsCompleted = levelsCompleted;
      //  this.world=world;
    }

    @Override
    public void won()
    {
     //   if ( (levelsCompleted.highestLevelCompleted( levelsDir ) < levelNumber) &&  (world.life > 0 )   )
          if  (levelsCompleted.highestLevelCompleted( levelsDir ) < levelNumber)
        {
            levelsCompleted.setCompletedLevel( levelsDir, levelNumber );
        }
    }

    @Override
    public void lost()
    {
    }
}
