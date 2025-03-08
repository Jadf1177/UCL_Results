package com.example.UCL_Results;

public class TeamLogoHelper {
    public static int getLogoResource(String teamName) {
        if (teamName == null) return R.drawable.default_team_logo;

        switch (teamName.toLowerCase()) {
            // Group A
            case "paris saint-germain": return R.drawable.psg_logo;
            case "real madrid": return R.drawable.real_madrid_logo;
            case "club brugge": return R.drawable.club_brugge_logo;
            case "galatasaray": return R.drawable.galatasaray_logo;

            // Group B
            case "bayern munich": return R.drawable.bayern_logo;
            case "tottenham hotspur": return R.drawable.tottenham_logo;
            case "olympiacos": return R.drawable.olympiacos_logo;
            case "red star belgrade": return R.drawable.red_star_logo;

            // Group C
            case "manchester city": return R.drawable.man_city_logo;
            case "atalanta": return R.drawable.atalanta_logo;
            case "shakhtar donetsk": return R.drawable.shakhtar_logo;
            case "dinamo zagreb": return R.drawable.dinamo_zagreb_logo;

            // Group D
            case "juventus": return R.drawable.juventus_logo;
            case "atletico madrid": return R.drawable.atletico_logo;
            case "bayer leverkusen": return R.drawable.leverkusen_logo;
            case "lokomotiv moscow": return R.drawable.lokomotiv_logo;

            // Group E
            case "liverpool": return R.drawable.liverpool_logo;
            case "napoli": return R.drawable.napoli_logo;
            case "red bull salzburg": return R.drawable.salzburg_logo;
            case "krc genk": return R.drawable.genk_logo;

            // Group F
            case "barcelona": return R.drawable.barcelona_logo;
            case "borussia dortmund": return R.drawable.dortmund_logo;
            case "inter milan": return R.drawable.inter_logo;
            case "slavia prague": return R.drawable.slavia_prague_logo;

            // Group G
            case "rb leipzig": return R.drawable.leipzig_logo;
            case "lyon": return R.drawable.lyon_logo;
            case "benfica": return R.drawable.benfica_logo;
            case "zenit st petersburg": return R.drawable.zenit_logo;

            // Group H
            case "chelsea": return R.drawable.chelsea_logo;
            case "ajax": return R.drawable.ajax_logo;
            case "valencia": return R.drawable.valencia_logo;
            case "lille": return R.drawable.lille_logo;

            default: return R.drawable.default_team_logo;
        }
    }
}