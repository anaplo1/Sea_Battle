package my.sea.battle.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    public static String encodePassword(String password) {
        try {
            // Создаем экземпляр класса MessageDigest с алгоритмом SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Кодируем пароль в байтовый массив, используя кодировку UTF-8
            byte[] encodedPassword = password.getBytes(StandardCharsets.UTF_8);

            // Применяем хэш-функцию SHA-256 к байтовому массиву пароля
            byte[] hash = digest.digest(encodedPassword);

            // Преобразуем хэш-значение в строку в шестнадцатеричном формате
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // Возвращаем закодированный пароль в шестнадцатеричном формате
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Обработка ошибки, если алгоритм хэширования не найден
            e.printStackTrace();
            return null;
        }
    }
}
