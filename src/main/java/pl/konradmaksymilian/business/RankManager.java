package pl.konradmaksymilian.business;

import pl.konradmaksymilian.persistence.PersistenceManager;
import pl.konradmaksymilian.persistence.RankRow;

import java.util.*;
import java.util.stream.Collectors;

public class RankManager {

    private final PersistenceManager db;
    private final Map<Integer, Rank> ranks;

    public RankManager(PersistenceManager db) {
        this.db = db;
        this.ranks = setUpRanks();
    }

    private Map<Integer, Rank> setUpRanks() {
        List<RankRow> ranksRows = db.getRanks().stream()
                .sorted(this::compare)
                .collect(Collectors.toList());

        Map<Integer, Map.Entry<String, List<String>>> rowRanks = new HashMap<>();

        ranksRows.forEach(rankRow -> {
            if (!rowRanks.containsKey(rankRow.getLevel())) {
                rowRanks.put(
                        rankRow.getLevel(),
                        new AbstractMap.SimpleEntry<>(
                                rankRow.getPrefix(),
                                addAll(new ArrayList<>(), rankRow.getPermissions()))
                );
            } else {
                addAll(rowRanks.get(rankRow.getLevel()).getValue(), rankRow.getPermissions());
            }
        });

        Map<Integer, Rank> ranks = new HashMap<>();
        rowRanks.forEach((level, entry) -> ranks.put(
                level,
                new Rank(
                        entry.getKey(),
                        Collections.unmodifiableList(entry.getValue())
                )
        ));

        return Collections.unmodifiableMap(ranks);
    }

    private int compare(RankRow one, RankRow two) {
        return two.getServer() - one.getServer();
    }

    private List<String> addAll(List<String> perms, String permsString) {
        List<String> rawPerms = Arrays.asList(permsString.split(";"));
        rawPerms.forEach(rawPerm -> {
            if (rawPerm.startsWith("!")) {
                removePerm(perms, rawPerm.substring(1));
            } else {
                perms.add(rawPerm);
            }
        });
        return perms;
    }

    private void removePerm(List<String> perms, String perm) {
        Iterator<String> iterator = perms.iterator();
        while (iterator.hasNext()) {
            String current = iterator.next();
            if (current.equals(perm)) {
                iterator.remove();
                return;
            }
        }
    }
}
