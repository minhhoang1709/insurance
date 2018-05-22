package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.CoverageCategory;

@Mapper
public interface CoverageCategoryMapper {
    @Select({
        "select",
        "coverage_category_id, name, description, type, created_date, update_date",
        "from public.coverage_category",
        "where coverage_category_id = #{coverageCategoryId,jdbcType=VARCHAR}"
    })
    CoverageCategory selectByCoverageCategoryId(String coverageCategoryId);

}