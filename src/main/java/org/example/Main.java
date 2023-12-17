package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.Scanner;

/**
 * Класс Main выполняет роль точки входа для программы
 */
public class Main {
    private static final Logger logger = LogManager.getLogger();
    private static final String ENG = "abcdefghijklmnopqrstuvwxyz";
    private static final String RUS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final String SP_CHARS = "!@#$%^&*()_-+=<>?";

    /**
     * Главный метод программы. Запрашивает у пользователя условия для генерации пароля и
     * выводит сгенерированный пароль и время его генерации.
     *
     * @param args массив аргументов командной строки, переданных при запуске программы
     */
    public static void main(String[] args) {
        logger.info("Запуск программы");
        Scanner scanner = new Scanner(System.in);
        int length;
        int languageCount;
        boolean useDifferentCase;
        boolean includeDigits;
        boolean useSpChars;
        String customDigits = "";
        // Ввод условий с консоли
        while (true) {
            try {
                System.out.print("Введите длину пароля: ");
                length = Integer.parseInt(scanner.nextLine());
                if (length <= 0) {
                    throw new IllegalArgumentException();
                } else {
                    logger.info("Принято значение переменной length");
                }

                System.out.print("Введите количество языков (1 или 2): ");
                languageCount = Integer.parseInt(scanner.nextLine());
                if (languageCount != 1 && languageCount != 2) {
                    throw new IllegalArgumentException();
                } else {
                    logger.info("Принято значение переменной languageCount");
                }

                System.out.print("Использовать разный регистр (true/false): ");
                useDifferentCase = Boolean.parseBoolean(scanner.nextLine());
                logger.info("Принято значение useDifferentCase");

                System.out.print("Включать цифры (true/false): ");
                includeDigits = Boolean.parseBoolean(scanner.nextLine());
                if (includeDigits) {
                    System.out.print("Введите список цифр, которые хотите использовать: ");
                    customDigits = scanner.nextLine();
                    //проверка того, состоит ли вся принятая строка из цифр
                    if (customDigits.matches("\\d+")) {
                        logger.info("Приняты пользовательские цифры");
                    } else {
                        throw new IllegalArgumentException();
                    }
                }

                System.out.print("Включать специальные символы (true/false): ");
                useSpChars = Boolean.parseBoolean(scanner.nextLine());
                logger.info("Принято значение useSpChars");

                break;
            } catch (IllegalArgumentException e) {
                logger.info("Некорректный ввод. Пожалуйста, попробуйте снова.");
            }
        }
        logger.info("Запуск секундомера");
        long startTime = System.currentTimeMillis();
        // Генерация пароля
        String password = generatePassword(length, languageCount, useDifferentCase, includeDigits, useSpChars, customDigits);
        logger.info("Финиш секундомера");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info("Время генерации пароля: " + executionTime + " миллисекунд");
        System.out.println("Сгенерированный пароль: " + password);
    }

    /**
     * Генерирует строку символов на основе количества языков, использования специальных символов и разных регистров.
     *
     * @param languageCount    количество используемых языков (при languageCount = 1 используется английский)
     * @param useSpChars       флаг, указывающий, нужно ли использовать специальные символы
     * @param useDifferentCase флаг, указывающий, нужно ли использовать разный регистр (при useDifferentCase = false используется нижний регистр)
     * @return созданная строка символов, которые могут войти в пароль
     */
    private static String getCharacters(int languageCount, boolean useSpChars, boolean useDifferentCase) {
        String characters = "";
        if (languageCount == 1) {
            characters += ENG;
            if (useDifferentCase) {
                characters += ENG.toUpperCase();
            }
        } else {
            characters += ENG + RUS;
            if (useDifferentCase) {
                characters += ENG.toUpperCase() + RUS.toUpperCase();
            }
        }
        if (useSpChars) {
            characters += SP_CHARS;
        }
        logger.info("Создана строка символов characters, которые могут войти в пароль");
        return characters;
    }

    /**
     * Метод для вставки пользовательских цифр в пароль, если включено их добавление.
     *
     * @param includeDigits   флаг, указывающий, нужно ли вставлять цифры
     * @param customDigits    строка с пользовательскими цифрами
     * @param passwordBuilder объект StringBuilder, представляющий пароль
     */
    private static void insertCustomDigits(boolean includeDigits, String customDigits, StringBuilder passwordBuilder) {
        if (includeDigits) {
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < customDigits.length(); i++) {
                int index = random.nextInt(passwordBuilder.length() + 1);
                passwordBuilder.insert(index, customDigits.charAt(i));
            }
            logger.info("Вставлены пользовательские цифры");
        }
    }

    /**
     * Метод для вставки случайных символов из предоставленной строки characters в passwordBuilder, указанной длины length.
     *
     * @param characters      строка символов, которые могут войти в пароль
     * @param length          длина генерируемого пароля
     * @param passwordBuilder объект StringBuilder, представляющий пароль
     */
    private static void insertCharacters(String characters, int length, StringBuilder passwordBuilder) {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            passwordBuilder.insert(random.nextInt(passwordBuilder.length() + 1), characters.charAt(index));
        }
        logger.info("Вставлены случайные символы из строки characters");
    }

    /**
     * Генерирует пароль заданной длины, используя определенные параметры.
     *
     * @param length           длина генерируемого пароля
     * @param languageCount    количество используемых языков
     * @param useDifferentCase флаг, указывающий, нужно ли использовать разный регистр
     * @param includeDigits    флаг, указывающий, нужно ли вставлять цифры
     * @param useSpChars       флаг, указывающий, нужно ли использовать специальные символы
     * @param customDigits     строка с пользовательскими цифрами
     * @return сгенерированный пароль
     */
    private static String generatePassword(int length, int languageCount, boolean useDifferentCase, boolean includeDigits, boolean useSpChars, String customDigits) {
        logger.info("Генерация пароля");
        StringBuilder passwordBuilder = new StringBuilder(length);
        String characters = getCharacters(languageCount, useSpChars, useDifferentCase);
        insertCustomDigits(includeDigits, customDigits, passwordBuilder);
        insertCharacters(characters, length - customDigits.length(), passwordBuilder);
        logger.info("Пароль сгенерирован");
        return passwordBuilder.toString();
    }
}