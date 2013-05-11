package net.argius.stew;

import java.io.*;
import java.nio.channels.*;
import java.util.*;
import java.util.regex.*;

/**
 * ConnectorConfiguration is a helper for ConnectorMap.
 */
public final class ConnectorConfiguration {

    static final String CONNECTOR_PROPERTIES_NAME = "connector.properties";

    private static final Pattern idPattern = Pattern.compile("^([^\\.]+)\\.name *=");

    /**
     * Loads configurations from a file.
     * @return
     * @throws IOException
     */
    public static ConnectorMap load() throws IOException {
        final File f = getPath();
        InputStream is = (f.exists())
                ? new FileInputStream(f)
                : new ByteArrayInputStream(new byte[0]);
        try {
            return load(is);
        } finally {
            is.close();
        }
    }

    /**
     * Loads configurations from a file.
     * @param is
     * @return
     * @throws IOException
     */
    public static ConnectorMap load(InputStream is) throws IOException {
        // cache for reuse
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        for (int c; (c = is.read(buffer)) >= 0;) {
            bos.write(buffer, 0, c);
        }
        bos.flush();
        // create ID list
        List<String> idList = new ArrayList<String>();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        try {
            for (String line; (line = reader.readLine()) != null;) {
                Matcher matcher = idPattern.matcher(line);
                if (matcher.find()) {
                    idList.add(matcher.group(1));
                }
            }
        } finally {
            reader.close();
        }
        // read as Properties
        Properties props = new Properties();
        props.load(new ByteArrayInputStream(bos.toByteArray()));
        // creates a instance
        return new ConnectorMap(idList, props);
    }

    /**
     * Saves configurations to a file.
     * @param map
     * @throws IOException
     */
    public static void save(ConnectorMap map) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        save(bos, map);
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        FileOutputStream fos = new FileOutputStream(getPath());
        try {
            fos.getChannel().transferFrom(Channels.newChannel(bis), 0, bytes.length);
        } finally {
            fos.close();
        }
    }

    /**
     * Saves configurations to a file.
     * @param os
     * @param map
     * @throws IOException
     */
    public static void save(OutputStream os, ConnectorMap map) throws IOException {
        // import using store
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        map.toProperties().store(bos, "");
        // lines to elements
        List<String> lines = new ArrayList<String>();
        Scanner scanner = new Scanner(new ByteArrayInputStream(bos.toByteArray()));
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().startsWith("#")) {
                    lines.add(line);
                }
            }
        } finally {
            scanner.close();
        }
        // rewrites records sorted by ID 
        Comparator<String> c = new ConnectorPropertyComparator(new ArrayList<String>(map.keySet()));
        Collections.sort(lines, c);
        PrintWriter out = new PrintWriter(os);
        try {
            for (String line : lines) {
                out.println(line);
            }
            out.flush();
        } finally {
            out.close();
        }
    }

    public static long lastModified() {
        return getPath().lastModified();
    }

    private static File getPath() {
        return Bootstrap.getSystemFile(CONNECTOR_PROPERTIES_NAME);
    }

    private static final class ConnectorPropertyComparator implements
                                                          Comparator<String>,
                                                          Serializable {

        private final List<String> idList;

        ConnectorPropertyComparator(List<String> idList) {
            this.idList = idList;
        }

        @Override
        public int compare(String s1, String s2) {
            int index1 = getIdIndex(s1);
            int index2 = getIdIndex(s2);
            if (index1 == index2) {
                return s1.compareTo(s2);
            }
            return index1 - index2;
        }

        private int getIdIndex(String s) {
            String[] sa = s.split("\\.", 2);
            if (sa.length >= 2) {
                String id = sa[0];
                return idList.indexOf(id);
            }
            return -1;
        }

    }

}
