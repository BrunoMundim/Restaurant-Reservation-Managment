package br.com.mundim.RestaurantReservationManagment.exceptions.config;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    public static final BaseErrorMessage ADDRESS_NOT_FOUND_BY_ID = new BaseErrorMessage("AddressNotFoundById");
    public static final BaseErrorMessage RESTAURANT_NOT_FOUND_BY_ID = new BaseErrorMessage("RestaurantNotFoundById");
    public static final BaseErrorMessage RESTAURANT_NOT_FOUND_BY_EMAIL = new BaseErrorMessage("RestaurantNotFoundByEmail");
    public static final BaseErrorMessage DINING_AREA_NOT_FOUND_BY_ID = new BaseErrorMessage("DiningAreaNotFoundById");
    public static final BaseErrorMessage CUSTOMER_NOT_FOUND_BY_ID = new BaseErrorMessage("CustomerNotFoundById");
    public static final BaseErrorMessage CUSTOMER_NOT_FOUND_BY_CPF = new BaseErrorMessage("CustomerNotFoundByCpf");
    public static final BaseErrorMessage CUSTOMER_NOT_FOUND_BY_EMAIL = new BaseErrorMessage("CustomerNotFoundByEmail");
    public static final BaseErrorMessage RESERVATION_NOT_FOUND_BY_ID = new BaseErrorMessage("ReservationNotFoundById");
    public static final BaseErrorMessage OPERATING_HOURS_NULL = new BaseErrorMessage("OperatingHoursNull");
    public static final BaseErrorMessage ADDRESS_NULL = new BaseErrorMessage("AddressNull");
    public static final BaseErrorMessage INCORRECT_WEEK_DAY = new BaseErrorMessage("IncorrectWeekDay");
    public static final BaseErrorMessage INCORRECT_OPERATING_HOUR_FORMAT = new BaseErrorMessage("IncorrectOperatingHourFormat");
    public static final BaseErrorMessage DUPLICATED_EMAIL = new BaseErrorMessage("DuplicatedEmail");
    public static final BaseErrorMessage DUPLICATED_CPF = new BaseErrorMessage("DuplicatedCPF");
    public static final BaseErrorMessage DUPLICATED_CNPJ = new BaseErrorMessage("DuplicatedCNPJ");
    public static final BaseErrorMessage RESERVATION_TIME_UNAVAILABLE = new BaseErrorMessage("ReservationTimeUnavailable");
    public static final BaseErrorMessage NO_DINING_AREA_AVAILABLE = new BaseErrorMessage("NoDiningAreaAvailable");
    public static final BaseErrorMessage NO_DINING_AREA_WITH_PARTY_SIZE = new BaseErrorMessage("NoDiningAreaWithSizeAsked");
    public static final BaseErrorMessage CONFLICTING_OPERATING_HOURS = new BaseErrorMessage("ConflictOperatingHours");

    // UNAUTHORIZED EXCEPTIONS
    public static final BaseErrorMessage UNAUTHORIZED_RESTAURANT_EXCEPTION = new BaseErrorMessage("UnauthorizedRestaurantException");
    public static final BaseErrorMessage UNAUTHORIZED_CUSTOMER_EXCEPTION = new BaseErrorMessage("UnauthorizedCustomerException");

    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... params) {
        this.params = Arrays.copyOf(params, params.length);
        return this;
    }

    public String getMessage() {
        String message = tryGetMessageFromBundle();
        if(params != null && params.length > 0) {
            MessageFormat fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }

    private String tryGetMessageFromBundle() {
        return getResource().getString(key);
    }

    public ResourceBundle getResource() {
        return ResourceBundle.getBundle("messages");
    }

}
