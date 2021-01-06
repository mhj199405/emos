package com.tongtech.auth.auth_common;//package com.tongtech.auth.auth_common;
//
//import org.hibernate.boot.model.naming.Identifier;
//import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
//import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
//
//import static com.google.common.base.CaseFormat.*;
//
//public class SnakeCaseNamingStrategy extends PhysicalNamingStrategyStandardImpl {
//	private static final long serialVersionUID = 1L;
//
//	public static final org.ch.ch_common.SnakeCaseNamingStrategy INSTANCE = new org.ch.ch_common.SnakeCaseNamingStrategy();
//
//    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
//	    return new Identifier(
//	        UPPER_CAMEL.to(LOWER_UNDERSCORE, name.getText()),
//	        name.isQuoted()
//	    );
//    }
//
//    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
//	    return new Identifier(
//	        LOWER_CAMEL.to(LOWER_UNDERSCORE, name.getText()),
//	        name.isQuoted()
//	    );
//    }
//}
