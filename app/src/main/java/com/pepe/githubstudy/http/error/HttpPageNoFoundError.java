

package com.pepe.githubstudy.http.error;

/**
 * Created by ThirtyDegreesRay on 2017/8/28 16:59:37
 */

public class HttpPageNoFoundError extends HttpError {
    public HttpPageNoFoundError() {
        super(HttpErrorCode.PAGE_NOT_FOUND);
    }
}
