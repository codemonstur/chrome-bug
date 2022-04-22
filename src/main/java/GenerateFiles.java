import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;
import static java.nio.file.StandardOpenOption.CREATE;

public class GenerateFiles {

    private static final String HTML_PAGE = """
            <html>
             <head>
               <meta http-equiv="Content-Security-Policy" content="default-src 'self'; style-src %s;">
               <style>%s</style>
             </head>
            <body class="background-color">Hello, world!</body>
            """;

    public static final String STYLE_WITH_CRLF =
            ".background-color {\n" +
            "    background-color: green;\n" +
            "}\n" +
            ".text-align-center {\r\n" +
            "    text-align: center;\r\n" +
            "}\n";

    public static final String STYLE_WITH_LF =
            ".background-color {\n" +
            "    background-color: green;\n" +
            "}\n" +
            ".text-align-center {\n" +
            "    text-align: center;\n" +
            "}\n";

    public static void main(final String... args) throws IOException, NoSuchAlgorithmException {
        final String fileOne = String.format(HTML_PAGE, toCspHash(STYLE_WITH_CRLF), STYLE_WITH_CRLF);
        final String fileTwo = String.format(HTML_PAGE, toCspHash(STYLE_WITH_LF), STYLE_WITH_LF);
        Files.writeString(Paths.get("target/file-one.html"), fileOne, WRITE, TRUNCATE_EXISTING, CREATE);
        Files.writeString(Paths.get("target/file-two.html"), fileTwo, WRITE, TRUNCATE_EXISTING, CREATE);
    }

    private static String toCspHash(final String data) throws NoSuchAlgorithmException {
        return "'sha256-"+encodeBase64(sha256(data.getBytes(UTF_8)))+"'";
    }
    private static byte[] sha256(final byte[] data) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-256").digest(data);
    }
    private static String encodeBase64(final byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }

}
