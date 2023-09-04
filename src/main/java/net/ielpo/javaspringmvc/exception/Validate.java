package net.ielpo.javaspringmvc.exception;

/**
 * @author Alberto Ielpo
 * @apiNote This class simulate Assert.isTrue but it throws a
 *          ClientRequestException | ServerRequestException instead managed by
 *          GlobalExceptionHandler
 */
public class Validate {

    /**
     * If condition is false then throw ClientRequestException(message). This case
     * is managed by GlobalExceptionHandler
     * 
     * @param condition
     * @param message
     * @throws ClientRequestException
     */
    public static void or400(boolean condition, String message) throws ClientRequestException {
        if (!condition) {
            throw new ClientRequestException(message);
        }
    }

    /**
     * If condition is false then throw ServerRequestException(message). This case
     * is managed by GlobalExceptionHandler
     * 
     * @param condition
     * @param message
     * @throws ServerRequestException
     */
    public static void or500(boolean condition, String message) throws ServerRequestException {
        if (!condition) {
            throw new ServerRequestException(message);
        }
    }

}
