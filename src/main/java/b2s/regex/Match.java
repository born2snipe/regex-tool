package b2s.regex;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private List<Group> groups = new ArrayList<Group>();

        public void add(Group group) {
            groups.add(group);
        }

        public Group get(int index) {
            return groups.get(index);
        }

        public int getGroupCount() {
            return groups.size();
        }


    public static class Group {
        private int position, length;
        private String content;

        public Group(String content, int position, int length) {
            this.content = content;
            this.position = position;
            this.length = length;
        }

        public String getContent() {
            return content;
        }

        public int getLength() {
            return length;
        }

        public int getPosition() {
            return position;
        }
    }
}
