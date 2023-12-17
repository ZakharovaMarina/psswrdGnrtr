package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.Scanner;

/**
 * ����� Main ��������� ���� ����� ����� ��� ���������
 */
public class Main {
    private static final Logger logger = LogManager.getLogger();
    private static final String ENG = "abcdefghijklmnopqrstuvwxyz";
    private static final String RUS = "��������������������������������";
    private static final String SP_CHARS = "!@#$%^&*()_-+=<>?";

    /**
     * ������� ����� ���������. ����������� � ������������ ������� ��� ��������� ������ �
     * ������� ��������������� ������ � ����� ��� ���������.
     *
     * @param args ������ ���������� ��������� ������, ���������� ��� ������� ���������
     */
    public static void main(String[] args) {
        logger.info("������ ���������");
        Scanner scanner = new Scanner(System.in);
        int length;
        int languageCount;
        boolean useDifferentCase;
        boolean includeDigits;
        boolean useSpChars;
        String customDigits = "";
        // ���� ������� � �������
        while (true) {
            try {
                System.out.print("������� ����� ������: ");
                length = Integer.parseInt(scanner.nextLine());
                if (length <= 0) {
                    throw new IllegalArgumentException();
                } else {
                    logger.info("������� �������� ���������� length");
                }

                System.out.print("������� ���������� ������ (1 ��� 2): ");
                languageCount = Integer.parseInt(scanner.nextLine());
                if (languageCount != 1 && languageCount != 2) {
                    throw new IllegalArgumentException();
                } else {
                    logger.info("������� �������� ���������� languageCount");
                }

                System.out.print("������������ ������ ������� (true/false): ");
                useDifferentCase = Boolean.parseBoolean(scanner.nextLine());
                logger.info("������� �������� useDifferentCase");

                System.out.print("�������� ����� (true/false): ");
                includeDigits = Boolean.parseBoolean(scanner.nextLine());
                if (includeDigits) {
                    System.out.print("������� ������ ����, ������� ������ ������������: ");
                    customDigits = scanner.nextLine();
                    //�������� ����, ������� �� ��� �������� ������ �� ����
                    if (customDigits.matches("\\d+")) {
                        logger.info("������� ���������������� �����");
                    } else {
                        throw new IllegalArgumentException();
                    }
                }

                System.out.print("�������� ����������� ������� (true/false): ");
                useSpChars = Boolean.parseBoolean(scanner.nextLine());
                logger.info("������� �������� useSpChars");

                break;
            } catch (IllegalArgumentException e) {
                logger.info("������������ ����. ����������, ���������� �����.");
            }
        }
        logger.info("������ �����������");
        long startTime = System.currentTimeMillis();
        // ��������� ������
        String password = generatePassword(length, languageCount, useDifferentCase, includeDigits, useSpChars, customDigits);
        logger.info("����� �����������");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info("����� ��������� ������: " + executionTime + " �����������");
        System.out.println("��������������� ������: " + password);
    }

    /**
     * ���������� ������ �������� �� ������ ���������� ������, ������������� ����������� �������� � ������ ���������.
     *
     * @param languageCount    ���������� ������������ ������ (��� languageCount = 1 ������������ ����������)
     * @param useSpChars       ����, �����������, ����� �� ������������ ����������� �������
     * @param useDifferentCase ����, �����������, ����� �� ������������ ������ ������� (��� useDifferentCase = false ������������ ������ �������)
     * @return ��������� ������ ��������, ������� ����� ����� � ������
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
        logger.info("������� ������ �������� characters, ������� ����� ����� � ������");
        return characters;
    }

    /**
     * ����� ��� ������� ���������������� ���� � ������, ���� �������� �� ����������.
     *
     * @param includeDigits   ����, �����������, ����� �� ��������� �����
     * @param customDigits    ������ � ����������������� �������
     * @param passwordBuilder ������ StringBuilder, �������������� ������
     */
    private static void insertCustomDigits(boolean includeDigits, String customDigits, StringBuilder passwordBuilder) {
        if (includeDigits) {
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < customDigits.length(); i++) {
                int index = random.nextInt(passwordBuilder.length() + 1);
                passwordBuilder.insert(index, customDigits.charAt(i));
            }
            logger.info("��������� ���������������� �����");
        }
    }

    /**
     * ����� ��� ������� ��������� �������� �� ��������������� ������ characters � passwordBuilder, ��������� ����� length.
     *
     * @param characters      ������ ��������, ������� ����� ����� � ������
     * @param length          ����� ������������� ������
     * @param passwordBuilder ������ StringBuilder, �������������� ������
     */
    private static void insertCharacters(String characters, int length, StringBuilder passwordBuilder) {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            passwordBuilder.insert(random.nextInt(passwordBuilder.length() + 1), characters.charAt(index));
        }
        logger.info("��������� ��������� ������� �� ������ characters");
    }

    /**
     * ���������� ������ �������� �����, ��������� ������������ ���������.
     *
     * @param length           ����� ������������� ������
     * @param languageCount    ���������� ������������ ������
     * @param useDifferentCase ����, �����������, ����� �� ������������ ������ �������
     * @param includeDigits    ����, �����������, ����� �� ��������� �����
     * @param useSpChars       ����, �����������, ����� �� ������������ ����������� �������
     * @param customDigits     ������ � ����������������� �������
     * @return ��������������� ������
     */
    private static String generatePassword(int length, int languageCount, boolean useDifferentCase, boolean includeDigits, boolean useSpChars, String customDigits) {
        logger.info("��������� ������");
        StringBuilder passwordBuilder = new StringBuilder(length);
        String characters = getCharacters(languageCount, useSpChars, useDifferentCase);
        insertCustomDigits(includeDigits, customDigits, passwordBuilder);
        insertCharacters(characters, length - customDigits.length(), passwordBuilder);
        logger.info("������ ������������");
        return passwordBuilder.toString();
    }
}