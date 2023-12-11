package ex1a;

import java.util.*;
import java.util.stream.*;

// Mazeクラスは迷路を解くためのクラスです。
public class Maze {
    // メインメソッドです。ここからプログラムの実行が始まります。
    public static void main(String[] args) {
        var maze = new Maze();
        maze.solve();
    }

    // map は、各ノードとその子ノードのリストを持つマップです。
    //これにより、グラフの構造が定義されます。
    Map<String, List<String>> map = Map.of(
        "A", List.of("B", "C", "D"),
        "B", List.of("E", "F"),
        "C", List.of("G", "H"),
        "D", List.of("I", "J"));

    // solveメソッドは迷路を解くために、search メソッドを呼び出します。
    public void solve() {
        if (search("A") != null)
            System.out.println("found");
    }

    // search メソッドは、目的のノードを見つけるための探索を行います。
    String search(String root) {
        List<String> openList = new ArrayList<String>();
        openList.add(root);

        // openList にノードが存在する限りループを続けます。
        while (openList.size() > 0) {
            var state = get(openList);

            if (isGoal(state))
                return state;

            var children = children(state);
            openList = concat(openList, children);
        }

        // ゴールが見つからなかった場合は null を返します。
        return null;
    }

    // isGoal メソッドは、現在の状態がゴール（目標）かどうかを判断します。
    boolean isGoal(String state) {
        return "E".equals(state);
    }

    // get メソッドは、リストから最初の要素を削除し、その要素を返します。
    String get(List<String> list) {
        return list.remove(0);
    }

    // children メソッドは、現在のノードの子ノードを取得します。
    List<String> children(String current) {
        return this.map.getOrDefault(current, Collections.emptyList());
    }

    // concat メソッドは、2つのリストを結合します。
    List<String> concat(List<String> xs, List<String> ys) {
        return toMutable(Stream.concat(xs.stream(), ys.stream()).toList());
    }

    // toMutable メソッドは、不変リストを可変リストに変換します。
    List<String> toMutable(List<String> list) {
        return new ArrayList<String>(list);
    }
}
