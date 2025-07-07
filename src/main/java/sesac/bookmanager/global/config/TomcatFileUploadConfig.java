package sesac.bookmanager.global.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class TomcatFileUploadConfig {

    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {

            @Override
            protected void postProcessContext(Context context) {
                super.postProcessContext(context);

                try {
                    // 리플렉션을 사용하여 강제로 설정
                    Class<?> contextClass = context.getClass();

                    // setMaxParameterCount 메서드 호출
                    try {
                        Method setMaxParameterCount = contextClass.getMethod("setMaxParameterCount", int.class);
                        setMaxParameterCount.invoke(context, -1);
                        System.out.println("✅ setMaxParameterCount(-1) 성공");
                    } catch (Exception e) {
                        System.out.println("❌ setMaxParameterCount 실패: " + e.getMessage());
                    }

                    // setMaxPostSize 메서드 호출
                    try {
                        Method setMaxPostSize = contextClass.getMethod("setMaxPostSize", int.class);
                        setMaxPostSize.invoke(context, -1);
                        System.out.println("✅ setMaxPostSize(-1) 성공");
                    } catch (Exception e) {
                        System.out.println("❌ setMaxPostSize 실패: " + e.getMessage());
                    }

                    // 추가 multipart 설정
                    try {
                        Method setAllowCasualMultipartParsing = contextClass.getMethod("setAllowCasualMultipartParsing", boolean.class);
                        setAllowCasualMultipartParsing.invoke(context, true);
                        System.out.println("✅ setAllowCasualMultipartParsing(true) 성공");
                    } catch (Exception e) {
                        System.out.println("❌ setAllowCasualMultipartParsing 실패: " + e.getMessage());
                    }

                    System.out.println("🔧 Tomcat Context 설정 완료");

                } catch (Exception e) {
                    System.out.println("❌ Tomcat Context 설정 전체 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            protected void customizeConnector(Connector connector) {
                super.customizeConnector(connector);

                if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>) {
                    AbstractHttp11Protocol<?> protocol = (AbstractHttp11Protocol<?>) connector.getProtocolHandler();
                    protocol.setMaxSwallowSize(-1);
                    protocol.setMaxHttpRequestHeaderSize(100 * 1024);
                    System.out.println("✅ Connector 설정 완료: maxSwallowSize=-1");
                }
            }
        };
    }
}