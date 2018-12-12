package hpcgateway.wp.orm.beans;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

public class PasswordHelper
{
	final public static int SALT_LENGTH = 12;
	
	public static boolean validPassword(String password, String passwordInDb) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		// 字符串格式口令转换成字节数组
		byte[] pwdInDb = Base64.decodeBase64(passwordInDb);
		// 声明盐变量
		byte[] salt = new byte[SALT_LENGTH];
		// 将盐从数据库中保存的口令字节数组中提取出来
		System.arraycopy(pwdInDb, 0, salt, 0, SALT_LENGTH);
		// 创建消息摘要对象
		MessageDigest md = MessageDigest.getInstance("MD5");
		// 将盐数据传入消息摘要对象
		md.update(salt);
		// 将口令的数据传给消息摘要对象
		md.update(password.getBytes("UTF-8"));
		// 生成输入口令的消息摘要
		byte[] digest = md.digest();
		// 声明一个保存数据库中口令消息摘要的变量
		byte[] digestInDb = new byte[pwdInDb.length - SALT_LENGTH];
		// 取得数据库中口令的消息摘要
		System.arraycopy(pwdInDb, SALT_LENGTH, digestInDb, 0, digestInDb.length);
		// 比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
		// 如果相等密码正确，否则错误
		return Arrays.equals(digest, digestInDb);
	}
	 
	public static String encryptedPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		// 随机数生成器
		SecureRandom random = new SecureRandom();
		// 声明盐数组变量 12
		byte[] salt = new byte[SALT_LENGTH];
		// 将随机数放入盐变量中
		random.nextBytes(salt);

		// 创建消息摘要
		MessageDigest md = MessageDigest.getInstance("MD5");
		// 将盐数据传入消息摘要对象
		md.update(salt);
		// 将口令的数据传给消息摘要对象
		md.update(password.getBytes("UTF-8"));
		// 获得消息摘要的字节数组
		byte[] digest = md.digest();

		// 因为要在口令的字节数组中存放盐，所以加上盐的字节长度
		byte[] pwd = new byte[digest.length + SALT_LENGTH];
		// 将盐的字节拷贝到生成的加密口令字节数组的前12个字节，以便在验证口令时取出盐
		System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
		// 将消息摘要拷贝到加密口令字节数组从第13个字节开始的字节
		System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
		// 将字节数组格式加密后的口令转化为Base64可读形式
		return Base64.encodeBase64String(pwd);
	}
}
