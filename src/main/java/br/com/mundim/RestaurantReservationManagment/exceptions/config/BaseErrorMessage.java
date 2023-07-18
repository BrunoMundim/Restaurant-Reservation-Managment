package br.com.mundim.RestaurantReservationManagment.exceptions.config;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    public static final BaseErrorMessage ADDRESS_NOT_FOUND_BY_ID = new BaseErrorMessage("AddressNotFoundById");
    public static final BaseErrorMessage RESTAURANT_NOT_FOUND_BY_ID = new BaseErrorMessage("RestaurantNotFoundById");
    public static final BaseErrorMessage DINING_AREA_NOT_FOUND_BY_ID = new BaseErrorMessage("DiningAreaNotFoundById");
    public static final BaseErrorMessage OPERATING_HOURS_NULL = new BaseErrorMessage("OperatingHoursNull");
    public static final BaseErrorMessage ADDRESS_NULL = new BaseErrorMessage("AddressNull");
    public static final BaseErrorMessage INCORRECT_WEEK_DAY = new BaseErrorMessage("IncorrectWeekDay");
    public static final BaseErrorMessage INCORRECT_OPERATING_HOUR_FORMAT = new BaseErrorMessage("IncorrectOperatingHourFormat");
    public static final BaseErrorMessage DINING_AREA_RESERVED_FOR_ANOTHER_CUSTOMER = new BaseErrorMessage("DiningAreaReservedForAnotherCustomer");

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
