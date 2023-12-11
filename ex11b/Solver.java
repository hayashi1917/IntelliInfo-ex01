package ex1b;

import java.util.*;
import java.util.stream.*;

interface World extends Cloneable {
    boolean isValid();  // このWorldの状態が有効かを判定
    boolean isGoal();   // このWorldの状態がゴールかを判定
    List<Action> actions();  // このWorldの状態から可能なアクションのリストを返す
    World successor(Action action);  // 与えられたアクションを実行した後の新しいWorldの状態を返す
}

interface Action {
    // アクションを表すインターフェース。具体的なアクションはこのインターフェースを実装する。
}


class State {
    State parent;  // この状態に至る前の状態
    Action action;  // この状態に至るために取られたアクション
    World world;  // この状態のWorldインターフェースの実装

    // コンストラクタで状態を生成
    State(State parent, Action action, World child) {
        this.action = action;
        this.parent = parent;
        this.world = child;
    }

    // ゴール状態かどうかを判定
    public boolean isGoal() {
        return this.world.isGoal();
    }

    // この状態から派生する子供の状態のリストを生成
    List<State> children() {
        return this.world.actions().stream()
                .map(a -> new State(this, a, this.world.successor(a)))
                .filter(s -> s.world.isValid())
                .toList();
    }

    // 状態を文字列として出力
    @Override
    public String toString() {
        if (this.action != null) {
            return String.format("%s(%s)", this.world, this.action);
        } else {
            return this.world.toString();
        }
    }
}


public class Solver {
    // ソルバーのエントリーポイント。探索を開始し、ゴールを見つけたら解を出力
    public void solve(World world) {
        var root = new State(null, null, world);  // 初期状態
        var goal = search(root);  // 探索実行

        if (goal != null)
            printSolution(goal);  // ゴールが見つかれば解を出力
    }

    // 探索メソッド。幅優先探索のような振る舞いをする
    State search(State root) {
        var openList = toMutable(List.of(root));  // 探索対象のリスト

        while (!openList.isEmpty()) {
            var state = get(openList);  // リストから状態を取り出す

            if (state.isGoal())
                return state;  // ゴール状態なら探索終了

            var children = state.children();  // 子供状態を取得
            openList = concat(openList, children);  // 子供をリストに追加
        }

        return null;  // ゴールが見つからなければnullを返す
    }

    // リストから最初の要素を取り出す
    State get(List<State> list) {
        return list.remove(0);
    }

    // 二つのリストを結合する
    List<State> concat(List<State> xs, List<State> ys) {
        return toMutable(Stream.concat(xs.stream(), ys.stream()).toList());
    }

    // 不変リストを可変リストに変換する
    List<State> toMutable(List<State> list) {
        return new ArrayList<>(list);
    }

    // ゴールから初期状態までの経路を出力する
    void printSolution(State goal) {
        var list = new ArrayList<State>();

        while (goal != null) {
            list.add(0, goal);  // リストの先頭に追加
            goal = goal.parent;  // 親状態に移動
        }

        list.forEach(System.out::println);  // 解の状態を出力
    }
}

