package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContextWrapper;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private TreeNode head = new TreeNode('h');

    @PostConstruct
    public void init(){
        SensitiveFilter sensitiveFilter = new SensitiveFilter();
        InputStream is = sensitiveFilter.getClass().getClassLoader().getResourceAsStream("sensitiveWord.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ( (line = reader.readLine())!=null){
                this.addWord(line);
//                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean addWord(String str){
        TreeNode node = head;
        for (int i=0;i<str.length();i++){
            if (node.getSubNode(str.charAt(i))==null){
                node.addSubNode(str.charAt(i));
                if (i==str.length()-1){
                    node.getSubNode(str.charAt(i)).setFlag(true);
                    continue;
                }
            }
            node = node.getSubNode(str.charAt(i));
        }
        return true;
    }
    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }
    public String filter(String content){
        int headIndex = 0;
        int endIndex = 0;
        StringBuilder sb = new StringBuilder();
        String REPLACE = "***";
        boolean flag = true;
        TreeNode node = head;
        while (endIndex<content.length()){
            if(!isSymbol(content.charAt(endIndex))) {
                if (node.getSubNode(content.charAt(endIndex)) == null) {
                    if (node == head) {
                        sb.append(content.charAt(endIndex));
                        headIndex++;
                        endIndex = headIndex;
                        continue;
                    }
                    sb.append(content.substring(headIndex, endIndex));
                    headIndex = endIndex;
                    node = head;
                } else {
                    node = node.getSubNode(content.charAt(endIndex));
                    endIndex++;
                    if (node.getFlag()) {
                        sb.replace(headIndex,endIndex,"*".repeat(endIndex-headIndex));
//                        sb.append(REPLACE);
                        headIndex = endIndex;
                        node = head;
                    }
                }
            }else{
                endIndex++;
            }
        }
        return sb.toString();
    }

    private class TreeNode{
        private char data;
        private boolean flag;
        private Map<Character, TreeNode> son;
        public TreeNode(char c){
            this.data = c;
            this.flag = false;
            this.son = new HashMap<>();
        }
        public TreeNode(char c,boolean flag){
            this.data = c;
            this.flag = flag;
            this.son = new HashMap<>();
        }

        public boolean getFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void addSubNode(char c){
            if (son.containsKey(c)){
                return;
            }
            son.put(c,new TreeNode(c));
        }

        public TreeNode getSubNode(char c){
            if (son.containsKey(c)){
                return son.get(c);
            }
            return null;
        }


//        public void addKeyWord(String key){
//
//        }
    }

}
