import org.springframework.security.crypto.bcrypt.BCrypt;

/** 生成 BCrypt 密码哈希（可选工具） */
public class BcryptHash {
    public static void main(String[] args) {
        String raw = args.length > 0 ? args[0] : "123456";
        String hash = BCrypt.hashpw(raw, BCrypt.gensalt(10));
        System.out.println(hash);
        System.out.println("verify=" + BCrypt.checkpw(raw, hash));
    }
}
