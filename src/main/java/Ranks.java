import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Ranks {
    static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, String>> assetIdToEventBasedNotificationMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        String ipfsUrl = "\"https://ipfs.io/ipfs/QmbRh8CU99J8z5cvQ9fRjDegPVMT4zi7FbBcx2PagFga4f\"";
        ipfsUrl = ipfsUrl.substring(1,ipfsUrl.length()-1);
        System.out.println(ipfsUrl);

    }


    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private static void putRanks(List<Leader> leaders) {
        int rank = 1;
        int rankInc = 0;
        int score = leaders.get(0).getTotalScore();

        for (Leader leader : leaders) {
            if (leader.getTotalScore() == score) {
                leader.setRank(rank);
                rankInc += 1;
            } else if (leader.getTotalScore() < score) {
                rank += rankInc;
                rankInc = 1;
                score = leader.getTotalScore();
                leader.setRank(rank);
            }
        }
    }

    public static class Leader {

        private int rank;
        private int totalScore;
        private String publicAddress;

        public Leader() {
        }

        public Leader(int rank, int totalScore, String publicAddress) {
            this.rank = rank;
            this.totalScore = totalScore;
            this.publicAddress = publicAddress;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }

        public String getPublicAddress() {
            return publicAddress;
        }

        public void setPublicAddress(String publicAddress) {
            this.publicAddress = publicAddress;
        }

        @Override public String toString() {
            return "Leader{" + "rank=" + rank + ", totalScore=" + totalScore + ", publicAddress='" + publicAddress + '\'' + '}';
        }
    }
}
