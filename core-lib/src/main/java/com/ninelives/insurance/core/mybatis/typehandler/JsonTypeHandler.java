package com.ninelives.insurance.core.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ninelives.insurance.core.util.GsonLocalDateAdapter;
import com.ninelives.insurance.model.PolicyClaimDocumentExtra;

@MappedTypes(PolicyClaimDocumentExtra.class)
public class JsonTypeHandler<E> extends BaseTypeHandler<E> {
	private Class<E> type;
	
	public final static Gson gson = new GsonBuilder()
			.registerTypeAdapter(LocalDate.class, new GsonLocalDateAdapter())
	        .create();

	public JsonTypeHandler(final Class<E> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type argument cannot be null");
		}
		this.type = type;
	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
		if (jdbcType == null) {
			ps.setString(i, toJson(parameter));
		} else {
			ps.setObject(i, toJson(parameter), jdbcType.TYPE_CODE);
		}
	}

	@Override
	public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return fromJson(rs.getString(columnName));
	}

	@Override
	public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return fromJson(rs.getString(columnIndex));
	}

	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return fromJson(cs.getString(columnIndex));
	}
	
	private String toJson(E clazz){
		return gson.toJson(clazz);
	}
	
	private E fromJson(String str){
		return gson.fromJson(str, type);
	}
	
	
}
