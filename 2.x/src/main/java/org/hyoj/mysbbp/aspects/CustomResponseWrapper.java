package org.hyoj.mysbbp.aspects;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;

/**
 * API 응답 데이터를 작성할 때,
 * 1. FilterServletOutputStream 객체 호출
 * 2. write 메소드로 데이터 삽입
 * 3. 삽입된 데이터는 filterOutput 초기화 할때 인자로 넘겨줬던 output에 실제로 데이터가 쌓임
 * 4. 그래서 toByteArray() 메소드를 통해 byte[] 타입으로 실제 API 응답을 가져올 수 있음.
 */
public class CustomResponseWrapper extends HttpServletResponseWrapper {

    ByteArrayOutputStream output; // 바이트 스트림 데이터를 담기 위한 객체
    FilterServletOutputStream filterOutput;

    /**
     * output 초기화
     */
    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteArrayOutputStream();
    }

    /**
     * 위에서 초기화한 output 객체로 filterOutput 객체를 생성 후 리턴
     */
    @Override
    public ServletOutputStream getOutputStream() {
        if (filterOutput == null) {
            filterOutput = new FilterServletOutputStream(output);
        }
        return filterOutput;
    }

    public byte[] getDataStream() {
        return output.toByteArray();
    }

}
