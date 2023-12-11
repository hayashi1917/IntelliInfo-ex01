
package ex1b;
import java.util.*;
// エントリーポイントのmainメソッドを含むクラス。
public class MazeProblem {
    public static void main(String[] args) {
        var solver = new Solver();  // Solverインスタンスの作成
        solver.solve(new MazeWorld("A"));  // "A"の位置から探索を開始
    }
}

// 迷路における行動を表すクラス。Actionインターフェースを実装。
class MazeAction implements Action {
    String next;  // 次の移動先を表す

    // コンストラクタ。次の位置を引数に取る。
    MazeAction(String next) {
        this.next = next;
    }

    // 行動を文字列で表現するtoStringメソッドをオーバーライド。
    @Override
    public String toString() {
        return "move to " + this.next;  // "move to X"の形で出力
    }
}

// 迷路の状態を表すクラス。Worldインターフェースを実装。
class MazeWorld implements World {
    // 迷路の構造を表すマップ。各ノードから行けるノードのリストを持つ。
    Map<String, List<String>> map = Map.of(
        "A", List.of("B", "C", "D"),
        "B", List.of("E", "F"),
        "C", List.of("G", "H"),
        "D", List.of("I", "J")
    );

    String current;  // 現在位置を表す

    // コンストラクタ。現在位置を引数に取る。
    MazeWorld(String current) {
        this.current = current;
    }

    // 迷路の各状態は常に有効であると定義されている。
    @Override
    public boolean isValid() {
        return true;
    }

    // ゴール状態の判定。ここでは"E"がゴールとされている。
    @Override
    public boolean isGoal() {
        return "E".equals(this.current);
    }

    // 可能なアクションのリストを返す。現在位置から行ける位置へのアクションを作成。
    @Override
    public List<Action> actions() {
        return this.map.getOrDefault(current, Collections.emptyList()).stream()
            .map(p -> (Action)new MazeAction(p))  // 文字列からMazeActionオブジェクトを作成
            .toList();
    }

    // あるアクションを実行した後の新しい状態（World）を返す。
    @Override
    public World successor(Action action) {
        var a = (MazeAction)action;  // アクションをMazeActionにキャスト
        return new MazeWorld(a.next);  // 新しいMazeWorldインスタンスを生成して返す
    }

    // 現在位置を文字列で出力するtoStringメソッドをオーバーライド。
    @Override
    public String toString() {
        return this.current;
    }
}
