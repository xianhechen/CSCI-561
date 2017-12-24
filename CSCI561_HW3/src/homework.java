import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isUpperCase;
@SuppressWarnings("Duplicates")
public class homework {
    static class Term {
        private boolean negation = false;
        private List<String> parameters = new ArrayList<>();
        private String predicate;
        private String str = "";
        Term() {}
        Term (String s) {
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(s);
            if (s.substring(0, 1).matches("~")) this.negation = true;
            int start = s.indexOf('(');
            this.predicate = ((this.negation) ? s.substring(1, start) : s.substring(0, start));
            while(m.find()) {
                String [] arrOfStr = m.group(1).split(",");
                this.parameters.addAll(Arrays.asList(arrOfStr));
            }
            String tempNeg = !this.negation ? "" : "~";
            String tempPre = this.predicate;
            List<String> tempPara = this.parameters;
            StringBuilder tempStr = new StringBuilder(tempNeg + tempPre + "(");
            for (String s2 : tempPara) {
                tempStr.append(s2).append(",");
            }
            tempStr = new StringBuilder(tempStr.substring(0, tempStr.length() - 1));
            tempStr.append(")");
            this.str = tempStr.toString();
        }
    }

    // member variables
    private static int line_number = 1;
    private static int query_size = 0;
    private static Queue<Term> queue = new LinkedList<>();
    private static List<List<Term>> orList2 = new ArrayList<>();
    private static List<Term> list2 = new ArrayList<>();
    private static List<Term> track2 = new ArrayList<>();

    // this function pushes a term into a queue
    void queryHandler(String str) {
        Term t = new Term(str);
        queue.offer(t);
    }

    // this function deep copies a term
    Term deepCopyTerm(Term t1) {
        String tempNeg = !t1.negation ? "" : "~";
        String tempPre = t1.predicate;
        List<String> tempPara = t1.parameters;
        String tempStr = tempNeg + tempPre + "(";
        for (String s : tempPara) {
            tempStr = tempStr + s + ",";
        }
        tempStr = tempStr.substring(0, tempStr.length()-1) + ")";
        Term temp = new Term(tempStr);
        return temp;
    }

    // this function checks if a statement contains " | "
    void orHandler(String str) {
        if (str.contains("|")) {
            List<Term> temp = new ArrayList<>();
            String [] arrOfStr = str.split(" \\| ");
            for (String a : arrOfStr) {
                Term t = new Term(a);
                temp.add(t);
            }
            orList2.add(temp);
        } else {
            Term t = new Term(str);
            list2.add(t);
        }
    }

    // this is the main function for the inference process
    boolean inference(Term term) {
        List<Term> list = new ArrayList<>(list2);
        List<List<Term>> orList = new ArrayList<>(orList2);
        List<Term> track = new ArrayList<>(track2);
        // for each item in the queue
        Term t3 = deepCopyTerm(term);
        t3.negation = !t3.negation;
        // see if resolvable in single item list
        for (Term t : list) {
            if (resolvable(t, t3)) return true;
        }
        // add to KB
        list.add(t3);
        for (int i = 0; i != list.size(); i++) { // each single term
            Term t1 = list.get(i);
            for (int j = 0; j != orList.size(); j++) { // each multiple term
                Term t2 = new Term();
                for (Term t : orList.get(j)) {
                    if (resolvable(t1, t)) {
                        t2 = t;
                        break;
                    }
                }
                if (t2.str != "") {
                    List<Term> resolved = resolve(t1, t2, orList.get(j));
                    if (resolved.size() == 1) {
                        Term t = resolved.get(0);
                        Term e = deepCopyTerm(t);
                        e.negation = !e.negation;
                        boolean circle  = false;
                        for (int m = 0; m != track.size(); m++) {
                            if (resolvable(e, track.get(m))) circle = true;
                        }
                        if (!circle) {
                            Term tempTerm = deepCopyTerm(t);
                            for (int m = 0; m < list.size(); m++) {
                                if (resolvable(list.get(m), tempTerm)) return true;
                            }
                            list.add(tempTerm);
                            track.add(tempTerm);
                        }
                    } else {
                        orList.add(resolved);
                    }
                }
            }
        }
        return false;
    }

    // check if two terms equals
    boolean resolvable(Term term1, Term term2) {
        Map<String, String> vcMap1 = new HashMap<>();
        Map<String, String>  vcMap2 = new HashMap<>();
        if (term1.parameters.size() != term2.parameters.size() || term1.negation == term2.negation || !term1.predicate.equals(term2.predicate)) return false;
        List<String> l1 = term1.parameters;
        List<String> l2 = term2.parameters;
        for (int i = 0; i < l1.size(); i++) {
            String currP1 = l1.get(i);
            String currP2 = l2.get(i);
            if (isUpperCase(currP1.charAt(0)) && isUpperCase(currP2.charAt(0))) { // C - C
                if (!currP1.equals(currP2)) return false;
            }
            else if (isUpperCase(currP1.charAt(0)) && !isUpperCase(currP2.charAt(0))) { // C - v
                if (vcMap2.containsKey(currP2)) {
                    String value = vcMap2.get(currP2);
                    if (!value.equals(currP1)) return false;
                } else {
                    vcMap2.put(currP2, currP1);
                }
            }
            else if (!isUpperCase(currP1.charAt(0)) && !isUpperCase(currP2.charAt(0))) { // v - v
                if (vcMap1.containsKey(currP1) && !vcMap2.containsKey(currP2)) {
                    String value = vcMap1.get(currP1);
                    vcMap2.put(currP2,value);
                }
                else if (!vcMap1.containsKey(currP1) && vcMap2.containsKey(currP2)) {
                    String value = vcMap2.get(currP2);
                    vcMap1.put(currP1,value);
                }
                else if (vcMap1.containsKey(currP1) && vcMap2.containsKey(currP2)) {
                    String value1 = vcMap1.get(currP1);
                    String value2 = vcMap2.get(currP2);
                    if (!value1.equals(value2)) return false;
                }
            }
            else if (!isUpperCase(currP1.charAt(0)) && isUpperCase(currP2.charAt(0))) { // v - C
                if (vcMap1.containsKey(currP1)) {
                    String value = vcMap1.get(currP1);
                    if (!value.equals(currP2)) return false;
                } else {
                    vcMap1.put(currP1, currP2);
                }
            }
        }
        return true;
    }

    // this function resolves items, needs to be tested
    List<Term> resolve(Term t1, Term t2, List<Term> l) {
        List<Term> result = new ArrayList<>();
        Map<String, String> dict = new HashMap<>();
        for (int i = 0; i < t1.parameters.size(); i++) {
            dict.put(t2.parameters.get(i), t1.parameters.get(i));
        }
        for (int i = 0; i < l.size(); i++) {
            String tempNeg = !l.get(i).negation ? "" : "~";
            String tempPre = l.get(i).predicate;
            List<String> tempPara = l.get(i).parameters;
            String tempStr = tempNeg + tempPre + "(";
            for (String s : tempPara) {
                tempStr = tempStr + s + ",";
            }
            tempStr = tempStr.substring(0, tempStr.length()-1) + ")";
            Term temp = new Term(tempStr);
            if (!resolvable(temp, t1)) {
                for (int j = 0; j < temp.parameters.size(); j++) {
                    if (dict.containsKey(temp.parameters.get(j))) {
                        // replace this parameter with its value
                        String currParam = temp.parameters.get(j);
                        String replaceParam = dict.get(currParam);
                        temp.parameters.set(j, replaceParam);
                    }
                }
                result.add(temp);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        homework hw = new homework();
        try {
            BufferedReader in = new BufferedReader(new FileReader("input.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (line_number == 1) query_size = Integer.valueOf(str);
                if (line_number > 1 && line_number < query_size+2) hw.queryHandler(str);
                if (line_number > query_size+2) hw.orHandler(str);
                line_number++;
            }
            in.close();
        } catch (IOException e) {
        }
        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            while (queue.size()!=0) {
                Term term = queue.poll();
                String result = hw.inference(term) ? "TRUE" : "FALSE";
                writer.println(result);
            }
            writer.close();
        } catch (IOException e) {
        }
    }
}