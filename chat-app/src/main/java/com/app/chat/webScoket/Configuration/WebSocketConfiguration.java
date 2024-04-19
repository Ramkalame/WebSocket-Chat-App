package com.app.chat.webScoket.Configuration;

import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer{
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		registry.addEndpoint("/ws.end.point")
		.setAllowedOrigins("http://localhost:3000")
		.withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		registry.enableSimpleBroker("/topic/", "/queue/");
		registry.setApplicationDestinationPrefixes("/app.desti.pre");
//		registry.setUserDestinationPrefix("/user.desti.pre");
		
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		
		registration.interceptors(new ChannelInterceptor() {
			
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if(accessor !=null) {
					
					String connection = accessor.getHeader("simpMessageType").toString();
					if(connection.equals("CONNECT")|| connection.equals("SUBSCRIBE") || connection.equals("DISCONNECT"))
						return message;
					else if(connection.equals("MESSAGE")) {
						String token = accessor.getNativeHeader("token").toString().replaceAll("[\"\\[\\]]", "");
						String destination = accessor.getNativeHeader("destination").toString().replaceAll("[\"\\[\\]]", "");
						if(token.equals("Credentials") && (destination.equals("/app.desti.pre") || (destination.equals("/app.desti.pre")))) {
							return message;
						}
						
					}
					
					
				}
				return null;
			}
	
		});
	}
	
	
	
	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		
		DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
		resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		converter.setContentTypeResolver(resolver);
		messageConverters.add(converter);
		return false;
		
	}
	
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer () {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*")
                        .allowedOrigins("http://localhost:3001", "http://localhost:3000");
            }
        };
    }
	

}
