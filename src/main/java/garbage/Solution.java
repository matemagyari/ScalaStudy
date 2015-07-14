package garbage;

public class Solution {


    public int solution(int a, int b) {
        String result = helper("", String.valueOf(a), String.valueOf(b));
        return Integer.parseInt(result);
    }

    private String helper(String acc, String aStr,String bStr) {
        if (isEmpty(aStr)) {
            return acc + bStr;
        } else if (isEmpty(bStr)) {
            return acc + aStr;
        } else {
            String acc2 = acc + aStr.charAt(0) + bStr.charAt(0);
            String aStr2 = aStr.substring(1);
            String bStr2 = bStr.substring(1);
            return helper(acc2, aStr2, bStr2);
        }
    }

    private static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static void main(String... args) {
        Solution solution = new Solution();
        System.out.println(solution.solution(12, 45));
        System.out.println(solution.solution(0, 0));
        assert solution.solution(56,12) == 5162;
        assert solution.solution(12345,678) == 16273856;
        assert solution.solution(123,67890) == 16273890;
    }
}
