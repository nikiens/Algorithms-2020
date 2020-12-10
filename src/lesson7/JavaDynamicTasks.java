package lesson7;

import kotlin.NotImplementedError;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    // Time - O(mn) - Best/ Worst. Space - O(mn)
    public static String longestCommonSubSequence(String first, String second) {
        StringBuilder buf = new StringBuilder();

        int[][] lcs = new int[first.length() + 1][second.length() + 1];
        int m = first.length();
        int n = second.length();

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                lcs[i][j] = (first.charAt(i - 1) == second.charAt(j - 1))
                        ? lcs[i - 1][j - 1] + 1
                        : Math.max(lcs[i - 1][j], lcs[i][j - 1]);
            }
        }

        while (m > 0 && n > 0) {
            if (first.charAt(m - 1) == second.charAt(n - 1)) {
                buf.append(first.charAt(m - 1));
                m--;
                n--;
            } else if (lcs[m][n] == lcs[m - 1][n]) {
                m--;
            } else {
                n--;
            }
        }
        return buf.reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */

    // Time - O(NlgN) - Best/Worst;
    // Space - O(n)
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        List<Integer> result = new ArrayList<>();

        int[] P = new int[list.size()];
        int[] M = new int[list.size() + 1];

        int length = 0;

        for (int i = list.size() - 1; i >=0; i--) {
            int lo = 1;
            int hi = length;

            while (lo <= hi) {
                int middle = (lo + hi) / 2;

                if (list.get(M[middle]) < list.get(i)) {
                    hi = middle - 1;
                } else {
                    lo = middle + 1;
                }
            }
            P[i] = M[lo - 1];
            M[lo] = i;

            if (lo > length) {
                length = lo;
            }
        }
        int k = M[length];

        for (int i = 0; i < length ; i++) {
            result.add(list.get(k));
            k = P[k];
        }
        return result;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
