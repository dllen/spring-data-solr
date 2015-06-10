/*
 * Copyright 2012 - 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.solr.core.query;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.FacetOptions.FieldWithFacetParameters;

/**
 * @author Christoph Strobl
 * @author Francisco Spaeth
 */
public class FacetOptionsTests {

	@Test
	public void testFacetOptionsEmptyConstructor() {
		FacetOptions options = new FacetOptions();
		Assert.assertFalse(options.hasFacets());
		Assert.assertFalse(options.hasFields());
		Assert.assertFalse(options.hasFacetQueries());
	}

	@Test
	public void testFacetOptionsConstructorSingleField() {
		FacetOptions options = new FacetOptions(new SimpleField("field_1"));
		Assert.assertTrue(options.hasFacets());
		Assert.assertTrue(options.hasFields());
		Assert.assertEquals(1, options.getFacetOnFields().size());
		Assert.assertFalse(options.hasFacetQueries());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFacetOptionsConstructorSingleNullValueField() {
		new FacetOptions((SimpleField) null);
	}

	@Test
	public void testFacetOptionsConstructorSingleFieldname() {
		FacetOptions options = new FacetOptions("field_1");
		Assert.assertTrue(options.hasFacets());
		Assert.assertTrue(options.hasFields());
		Assert.assertEquals(1, options.getFacetOnFields().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFacetOptionsConstructorSingleNullValueFieldname() {
		new FacetOptions((String) null);
	}

	@Test
	public void testAddFacetOnField() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnField(new SimpleField("field_1"));
		options.addFacetOnField(new SimpleField("field_2"));

		Assert.assertEquals(2, options.getFacetOnFields().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnFieldNullValue() {
		new FacetOptions().addFacetOnField((Field) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnFieldWithoutFieldname() {
		new FacetOptions().addFacetOnField(new SimpleField(""));
	}

	@Test
	public void testAddFacetOnPivotWithFieldNames() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnPivot("field_1", "field2");
		Assert.assertTrue(options.hasFacets());
		Assert.assertTrue(options.hasPivotFields());
		Assert.assertEquals(1, options.getFacetOnPivots().size());
		Assert.assertTrue(options.hasFields());
		Assert.assertEquals("field_1,field2", options.getFacetOnPivots().get(0).getName());
	}

	@Test
	public void testAddFacetOnPivotWithField() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnPivot(new SimpleField("field_1"), new SimpleField("field2"));
		Assert.assertTrue(options.hasFacets());
		Assert.assertTrue(options.hasPivotFields());
		Assert.assertEquals(1, options.getFacetOnPivots().size());
		Assert.assertTrue(options.hasFields());
		Assert.assertEquals("field_1,field2", options.getFacetOnPivots().get(0).getName());
	}

	@Test
	public void testAddMultipleFacetOnPivotWithField() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnPivot(new SimpleField("field_1"), new SimpleField("field2"));
		options.addFacetOnPivot(new SimpleField("field_3"), new SimpleField("field4"));
		Assert.assertTrue(options.hasFacets());
		Assert.assertTrue(options.hasPivotFields());
		Assert.assertEquals(2, options.getFacetOnPivots().size());
		Assert.assertTrue(options.hasFields());
		Assert.assertEquals("field_1,field2", options.getFacetOnPivots().get(0).getName());
		Assert.assertEquals("field_3,field4", options.getFacetOnPivots().get(1).getName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnPivotWithoutFieldName() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnPivot("field_1", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnPivotWithNullFieldName() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnPivot("field_1", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnPivotWithEmptyField() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnPivot(new SimpleField("field_1"), new SimpleField(""));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnPivotWithNullField() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnPivot(new SimpleField("field_1"), null);
	}

	@Test
	public void testAddFacetOnQueryConstructorSingleQuery() {
		FacetOptions options = new FacetOptions(new SimpleQuery(new SimpleStringCriteria("field_1:[* TO 5]")));
		Assert.assertTrue(options.hasFacets());
		Assert.assertTrue(options.hasFacetQueries());
		Assert.assertEquals(1, options.getFacetQueries().size());
		Assert.assertFalse(options.hasFields());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnQueryConstructorSingleNullQuery() {
		new FacetOptions((SolrDataQuery) null);
	}

	@Test
	public void testAddFacetOnQuery() {
		FacetOptions options = new FacetOptions();
		options.addFacetQuery(new SimpleQuery(new SimpleStringCriteria("field_1:[* TO 5]")));
		options.addFacetQuery(new SimpleQuery(new SimpleStringCriteria("field_1:[6 TO *]")));

		Assert.assertTrue(options.hasFacets());
		Assert.assertTrue(options.hasFacetQueries());
		Assert.assertEquals(2, options.getFacetQueries().size());
		Assert.assertFalse(options.hasFields());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFacetOnNullQuery() {
		FacetOptions options = new FacetOptions();
		options.addFacetQuery(null);
	}

	@Test
	public void testSetFacetSort() {
		FacetOptions options = new FacetOptions();
		Assert.assertNotNull(options.getFacetSort());
		Assert.assertEquals(FacetOptions.DEFAULT_FACET_SORT, options.getFacetSort());

		options.setFacetSort(FacetSort.INDEX);
		Assert.assertEquals(FacetSort.INDEX, options.getFacetSort());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetFacetSortWithNullValue() {
		new FacetOptions().setFacetSort(null);
	}

	@Test
	public void testSetFacetLimit() {
		FacetOptions options = new FacetOptions();
		Assert.assertEquals(FacetOptions.DEFAULT_FACET_LIMIT, options.getFacetLimit());

		options.setFacetLimit(20);
		Assert.assertEquals(20, options.getFacetLimit());

		options.setFacetLimit(-1);
		Assert.assertEquals(-1, options.getFacetLimit());
	}

	@Test
	public void testSetFacetMinCount() {
		FacetOptions options = new FacetOptions();
		Assert.assertEquals(FacetOptions.DEFAULT_FACET_MIN_COUNT, options.getFacetMinCount());

		options.setFacetMinCount(20);
		Assert.assertEquals(20, options.getFacetMinCount());

		options.setFacetMinCount(-1);
		Assert.assertEquals(0, options.getFacetMinCount());
	}

	@Test
	public void testGetFieldsWithFacetParameters() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnField(new SimpleField("field_1"));
		options.addFacetOnField(new FieldWithFacetParameters("field_2").setPrefix("prefix"));

		Assert.assertEquals(2, options.getFacetOnFields().size());
		Assert.assertEquals(1, options.getFieldsWithParameters().size());
		Assert.assertEquals("field_2", options.getFieldsWithParameters().iterator().next().getName());
	}

	@Test
	public void testGetFieldsWithFacetParametersNoFieldsAvailable() {
		FacetOptions options = new FacetOptions();
		options.addFacetOnField(new SimpleField("field_1"));

		Assert.assertEquals(1, options.getFacetOnFields().size());
		Assert.assertTrue(options.getFieldsWithParameters().isEmpty());
	}

	@Test
	public void testHasFacetPrefix() {
		FacetOptions options = new FacetOptions();
		options.setFacetPrefix("prefix");
		Assert.assertTrue(options.hasFacetPrefix());
	}

	@Test
	public void testHasBlankFacetPrefix() {
		FacetOptions options = new FacetOptions();
		options.setFacetPrefix("  ");
		Assert.assertFalse(options.hasFacetPrefix());
	}

	@Test
	public void testHasNullFacetPrefix() {
		FacetOptions options = new FacetOptions();
		options.setFacetPrefix(null);
		Assert.assertFalse(options.hasFacetPrefix());
	}

}
