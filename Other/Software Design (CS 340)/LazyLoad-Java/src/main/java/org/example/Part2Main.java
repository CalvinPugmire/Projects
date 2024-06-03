package org.example;

public class Part2Main {
    public static void main(String[] args) {
        Part2ArrayClass array2d = new Part2ArrayClass(5,5);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                array2d.set(i, j, (int)(Math.random()*100));
            }
        }

        array2d.save("C:\\Users\\troop\\IdeaProjects\\LazyLoad-Java\\map.txt");



        Part2ArrayProxy arrayProxy = new Part2ArrayProxy("C:\\Users\\troop\\IdeaProjects\\LazyLoad-Java\\map.txt");

        System.out.println(arrayProxy.getArray2d());

        System.out.println(arrayProxy.get(2, 3));

        System.out.println(arrayProxy.getArray2d());

        arrayProxy.set(4,4, 15);
        System.out.println(arrayProxy.get(4, 4));

        System.out.println(arrayProxy.getArray2d());

        arrayProxy.save("C:\\Users\\troop\\IdeaProjects\\LazyLoad-Java\\map.txt");
    }
}