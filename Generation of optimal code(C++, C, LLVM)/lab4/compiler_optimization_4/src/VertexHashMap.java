import java.util.HashMap;


public class VertexHashMap<K, V> extends HashMap<K, V> {

    @Override
    public V get(Object key) {
        V retval = null;
        K vertex = (K)key;

        super.forEach((k, v) -> {
            if (k.hashCode() == vertex.hashCode()){}
                //retval = super.get(vertex);
        });

        return retval;
    }
}
