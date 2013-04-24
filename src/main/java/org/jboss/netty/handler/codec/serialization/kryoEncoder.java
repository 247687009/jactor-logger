package org.jboss.netty.handler.codec.serialization;

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;

import java.io.ObjectOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class kryoEncoder extends ObjectEncoder {
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

	private final int estimatedLength;
	private final static Kryo kryo = new Kryo();

	/**
	 * Creates a new encoder with the estimated length of 512 bytes.
	 */
	public kryoEncoder() {
		this(512);
	}

	/**
	 * Creates a new encoder.
	 * 
	 * @param estimatedLength
	 *            the estimated byte length of the serialized form of an object.
	 *            If the length of the serialized form exceeds this value, the
	 *            internal buffer will be expanded automatically at the cost of
	 *            memory bandwidth. If this value is too big, it will also waste
	 *            memory bandwidth. To avoid unnecessary memory copy or
	 *            allocation cost, please specify the properly estimated value.
	 */
	public kryoEncoder(int estimatedLength) {
		if (estimatedLength < 0) {
			throw new IllegalArgumentException("estimatedLength: " + estimatedLength);
		}
		this.estimatedLength = estimatedLength;
	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		ChannelBufferOutputStream bout = new ChannelBufferOutputStream(dynamicBuffer(estimatedLength, ctx.getChannel().getConfig().getBufferFactory()));
		bout.write(LENGTH_PLACEHOLDER);
		ObjectOutputStream oout = new CompactObjectOutputStream(bout);
		Output output = new Output(oout);
		kryo.writeObject(output, msg);
//		oout.writeObject(msg);
//		oout.flush();
//		oout.close();
		output.close();

		ChannelBuffer encoded = bout.buffer();
		encoded.setInt(0, encoded.writerIndex() - 4);
		return encoded;
	}

}
