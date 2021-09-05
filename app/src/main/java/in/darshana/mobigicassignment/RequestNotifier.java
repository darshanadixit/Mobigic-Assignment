package in.darshana.mobigicassignment;

import retrofit2.Response;

public interface RequestNotifier {

    void notifySuccess(Response<?> response);
    void notifyNoInternet();
    void notifyError(Throwable throwable);
    void notifyString(String s);
}
