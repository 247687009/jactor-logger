package org.jboss.netty.handler.codec.serialization;

import github.com.cp149.netty.client.MyLoggingEventVO;

import java.io.StreamCorruptedException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;

import ch.qos.logback.classic.spi.LoggingEventVO;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class kryoDecoder extends LengthFieldBasedFrameDecoder {
	private final ClassResolver classResolver;
	private final static Kryo kryo = new Kryo();

	public kryoDecoder(ClassResolver classResolver) {
		this(1048576, classResolver);
	}

	/**
	 * Creates a new decoder with the specified maximum object size.
	 * 
	 * @param maxObjectSize
	 *            the maximum byte length of the serialized object. if the
	 *            length of the received object is greater than this value,
	 *            {@link StreamCorruptedException} will be raised.
	 * @param classResolver
	 *            the {@link ClassResolver} which will load the class of the
	 *            serialized object
	 */
	public kryoDecoder(int maxObjectSize, ClassResolver classResolver) {
		 super(maxObjectSize, 0, 4, 0, 4);
		if (classResolver == null) {
			throw new NullPointerException("classResolver");
		}
		this.classResolver = classResolver;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {

		ChannelBuffer frame = (ChannelBuffer) super.decode(ctx, channel, buffer);
		if (frame == null) {
			return null;
		}
		Input input = new Input(new CompactObjectInputStream(new ChannelBufferInputStream(frame), classResolver));
		MyLoggingEventVO someObject = kryo.readObject(input, MyLoggingEventVO.class);
		input.close();
		return someObject;
	}
}
