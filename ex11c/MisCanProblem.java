package ex1c;

import java.util.*;

// 宣教師と人食い問題を解決するメインクラス
public class MisCanProblem {
    public static void main(String[] args) {
        // Solverインスタンスを作成する
        var solver = new Solver();
        // 3人の宣教師、3人のカンニバル、1つのボートで問題を解く
        solver.solve(new MisCanWorld(3, 3, 1));
    }
}

// 宣教師と人食い問題のアクションを表すクラス
class MisCanAction implements Action {
    int missionary; // 移動する宣教師の数
    int cannibal;   // 移動するカンニバルの数
    int boat;       // ボートの方向（1または-1）

    // 取りうる全アクションのリスト
    static List<Action> all = List.of(
        new MisCanAction(-2, 0, -1),
        new MisCanAction(-1, -1, -1),
        new MisCanAction(0, -2, -1),
        new MisCanAction(-1, 0, -1),
        new MisCanAction(0, -1, -1),
        new MisCanAction(+2, 0, +1),
        new MisCanAction(+1, +1, +1),
        new MisCanAction(0, +2, +1),
        new MisCanAction(+1, 0, +1),
        new MisCanAction(0, +1, +1)
    );

    // MisCanActionのコンストラクタ
    MisCanAction(int missionary, int cannibal, int boat) {
        this.missionary = missionary;
        this.cannibal = cannibal;
        this.boat = boat;
    }

    // アクションの文字列表現を返す
    public String toString() {
        var dir = this.boat < 0 ? "右へ" : "左へ"; // ボートの方向
        var m = Math.abs(this.missionary);
        var c = Math.abs(this.cannibal);
        return String.format("%d人の宣教師と%d人のカンニバルを%s移動", m, c, dir);
    }
}

// 宣教師と人食い問題のワールド状態を表すクラス
class MisCanWorld implements World {
    int missionary; // 出発地点の宣教師の数
    int cannibal;   // 出発地点のカンニバルの数
    int boat;       // ボートの位置（0または1）

    // MisCanWorldのコンストラクタ
    public MisCanWorld(int missionary, int cannibal, int boat) {
        this.missionary = missionary;
        this.cannibal = cannibal;
        this.boat = boat;
    }

    // 現在のワールド状態のコピーを作成する
    public MisCanWorld clone() {
        return new MisCanWorld(this.missionary, this.cannibal, this.boat);
    }

    // 現在のワールド状態が有効かどうかをチェックする
    public boolean isValid() {
        if (this.missionary < 0 || this.missionary > 3) return false;
        if (this.cannibal < 0 || this.cannibal > 3) return false;
        if (this.boat < 0 || this.boat > 1) return false;
        // 宣教師がカンニバルに数で劣る状態は無効
        if (this.missionary > 0 && this.missionary < this.cannibal) return false;
        if ((3 - this.missionary) > 0 && (3 - this.missionary) < (3 - this.cannibal)) return false;
        return true;
    }

    // ゴール状態に達したかどうかをチェックする
    public boolean isGoal() {
        return this.missionary == 0 && this.cannibal == 0;
    }

    // この状態から取りうるアクションのリストを返す
    public List<Action> actions() {
        return MisCanAction.all;
    }

    // 与えられたアクションを取った結果の状態を返す
    public World successor(Action action) {
        var a = (MisCanAction) action;
        var next = clone();
        next.missionary += a.missionary;
        next.cannibal += a.cannibal;
        next.boat += a.boat;
        return next;
    }

    // ワールド状態の文字列表現を返す
    public String toString() {
        var ms = List.of("...", "m..", "mm.", "mmm");
        var cs = List.of("...", "c..", "cc.", "ccc");
        var bs = List.of("|~~~b|", "|b~~~|");
        var ml = ms.get(this.missionary);
        var cl = cs.get(this.cannibal);
        var b = bs.get(this.boat);
        var mr = ms.get(3 - this.missionary);
        var cr = cs.get(3 - this.cannibal);
        return String.format("%s%s%s%s%s", ml, cl, b, mr, cr);
    }
}
