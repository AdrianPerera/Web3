import jnr.ffi.annotations.In;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Time {
    public static void main(String[] args) {

        ConcurrentHashMap<Long, Long> hm = new ConcurrentHashMap<>();

        // enter data into hashmap
        hm.put(1L, 98L);
        hm.put(2L, 90L);
        hm.put(3L, 96L);
        hm.put(4L, 99L);
        hm.put(5L, 97L);
        Map<Long, Long> hm1 = sortByLatestRedeemed(hm);

        // print the sorted hashmap
        for (Map.Entry<Long, Long> en : hm1.entrySet()) {
            System.out.println("Key = " + en.getKey() + ", Value = " + en.getValue());
        }

        List<Asset> assets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Asset a = new Asset(i, "some" + i);
            assets.add(a);
        }
        List<Long> listWithOrder = new ArrayList<>(hm1.keySet());
        List<Asset> unRedeemed = new ArrayList<>();
        List<Asset> redeemed = new ArrayList<>();
        for (Asset as : assets) {
            if (listWithOrder.contains(as.assetId)) {
                redeemed.add(as);
            }else{
                unRedeemed.add(as);
            }
        }

        redeemed.sort(Comparator.comparing(asset -> {
            return listWithOrder.indexOf(asset.getAssetId());
        }));
        redeemed.addAll(unRedeemed);

        redeemed.forEach(e -> System.out.println(e.toString()));



    }

    public static HashMap<Long, Long> sortByLatestRedeemed(final ConcurrentHashMap<Long, Long> hm) {
        List<Map.Entry<Long, Long>> list = new LinkedList<>(hm.entrySet());

        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        final HashMap<Long, Long> temp = new LinkedHashMap<>();
        for (Map.Entry<Long, Long> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }



    public static class Asset {
        private long assetId;
        private String name;

        public Asset(long assetId, String name) {
            this.assetId = assetId;
            this.name = name;
        }

        public long getAssetId() {
            return assetId;
        }

        public void setAssetId(long assetId) {
            this.assetId = assetId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override public String toString() {
            return "Asset{" + "assetId=" + assetId + ", name='" + name + '\'' + '}';
        }
    }
}
