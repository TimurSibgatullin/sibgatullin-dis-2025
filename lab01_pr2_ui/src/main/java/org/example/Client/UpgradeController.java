package org.example.Client;

import org.example.CommonFiles.PlayerStatsLevels;

public class UpgradeController {

    private final PlayerStatsLevels levels;

    public UpgradeController(PlayerStatsLevels levels) {
        this.levels = levels;
    }

    public void upgrade(int index) {
        switch (index) {
            case 1 -> levels.healing++;
            case 2 -> levels.damage++;
            case 3 -> levels.defense++;
            case 4 -> levels.fireRate++;
            case 5 -> levels.speed++;
            case 6 -> levels.bulletSpeed++;
        }
    }
}