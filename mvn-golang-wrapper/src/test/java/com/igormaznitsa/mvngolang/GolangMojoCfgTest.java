/*
 * Copyright 2016 Igor Maznitsa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.igormaznitsa.mvngolang;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

import static org.junit.Assert.*;

import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingRequest;

import com.igormaznitsa.mvngolang.utils.ProxySettings;

public class GolangMojoCfgTest extends AbstractMojoTestCase {

  private <T extends AbstractGolangMojo> T findMojo(final Class<T> klazz, final String pomName, final String goal) throws Exception {
    final File pomFile = new File(GolangMojoCfgTest.class.getResource(pomName).toURI());
    final MavenExecutionRequest executionRequest = new DefaultMavenExecutionRequest();
    final ProjectBuildingRequest buildingRequest = executionRequest.getProjectBuildingRequest();
    final ProjectBuilder projectBuilder = this.lookup(ProjectBuilder.class);
    final MavenProject project = projectBuilder.build(pomFile, buildingRequest).getProject();
    return klazz.cast(this.lookupConfiguredMojo(project, goal));
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  private static void assertEqualsPath(final String etalon, final String toCheck) {
    final String normalizedEtalon = etalon.replace('\\', '/');
    final String normalizedToCheck = toCheck.replace('\\', '/');
    assertEquals("Wrong path : "+toCheck+" instead of "+etalon,normalizedEtalon,normalizedToCheck);
  }
  
  @Test
  public void testGolangCustomMojoConfiguration() throws Exception {
    final GolangCustomMojo customMojo = findMojo(GolangCustomMojo.class, "mojoCustom.xml","custom");
    assertTrue(customMojo.isUseMavenProxy());
    assertFalse(customMojo.isSkip());
    assertNull(customMojo.getProxy());
    assertNull(customMojo.getTargetArch());
    assertNull(customMojo.getTargetOS());
    assertNull(customMojo.getTargetArm());
    assertFalse(customMojo.isUseEnvVars());
    assertEquals("someCustomCommand",customMojo.getGoCommand());
    assertFalse(customMojo.isVerbose());
    assertTrue(customMojo.isHideBanner());
    assertEqualsPath("some/sources", customMojo.getSources(false).getPath());
    assertEqualsPath("some/root", customMojo.getGoRoot());
    assertEqualsPath("some/path", customMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},customMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},customMojo.getBuildFlags());
    assertNull(customMojo.getErrLogFile());
    assertNull(customMojo.getOutLogFile());
    assertNotNull(customMojo.getReportsFolder());
    assertFalse(customMojo.isIgnoreErrorExitCode());
  }

  @Test
  public void testGolangCleanMojoConfiguration() throws Exception {
    final GolangCleanMojo cleanMojo = findMojo(GolangCleanMojo.class, "mojoClean.xml", "clean");
    assertFalse(cleanMojo.isUseMavenProxy());
    assertFalse(cleanMojo.isSkip());
    assertNull(cleanMojo.getProxy());
    assertNull(cleanMojo.getTargetArch());
    assertNull(cleanMojo.getTargetOS());
    assertNull(cleanMojo.getTargetArm());
    assertFalse(cleanMojo.isUseEnvVars());
    assertEquals("clean",cleanMojo.getGoCommand());
    assertFalse(cleanMojo.isVerbose());
    assertTrue(cleanMojo.isHideBanner());
    assertEqualsPath("some/sources", cleanMojo.getSources(false).getPath());
    assertEqualsPath("some/root", cleanMojo.getGoRoot());
    assertEqualsPath("some/path", cleanMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},cleanMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},cleanMojo.getBuildFlags());
  }

  @Test
  public void testGolangFixMojoConfiguration() throws Exception {
    final GolangFixMojo fixMojo = findMojo(GolangFixMojo.class,"mojoFix.xml","fix");
    assertFalse(fixMojo.isUseMavenProxy());
    assertFalse(fixMojo.isSkip());
    assertNull(fixMojo.getProxy());
    assertFalse(fixMojo.isUseEnvVars());
    assertNull(fixMojo.getTargetArm());
    assertEquals("fix",fixMojo.getGoCommand());
    assertFalse(fixMojo.isVerbose());
    assertTrue(fixMojo.isHideBanner());
    assertEqualsPath("some/sources", fixMojo.getSources(false).getPath());
    assertEqualsPath("some/root", fixMojo.getGoRoot());
    assertEqualsPath("some/path", fixMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},fixMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},fixMojo.getBuildFlags());
  }

  @Test
  public void testGolangFmtMojoConfiguration() throws Exception {
    final GolangFmtMojo fmtMojo = findMojo(GolangFmtMojo.class, "mojoFmt.xml", "fmt");
    assertFalse(fmtMojo.isUseMavenProxy());
    assertFalse(fmtMojo.isSkip());
    assertNull(fmtMojo.getProxy());
    assertFalse(fmtMojo.isUseEnvVars());
    assertNull(fmtMojo.getTargetArm());
    assertEquals("fmt",fmtMojo.getGoCommand());
    assertFalse(fmtMojo.isVerbose());
    assertTrue(fmtMojo.isHideBanner());
    assertEqualsPath("some/sources", fmtMojo.getSources(false).getPath());
    assertEqualsPath("some/root", fmtMojo.getGoRoot());
    assertEqualsPath("some/path", fmtMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},fmtMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},fmtMojo.getBuildFlags());
  }

  @Test
  public void testGolangGenerateMojoConfiguration() throws Exception {
    final GolangGenerateMojo genMojo = findMojo(GolangGenerateMojo.class,"mojoGenerate.xml","generate");
    assertFalse(genMojo.isUseMavenProxy());
    assertFalse(genMojo.isSkip());
    assertNull(genMojo.getProxy());
    assertFalse(genMojo.isUseEnvVars());
    assertNull(genMojo.getTargetArm());
    assertEquals("generate",genMojo.getGoCommand());
    assertFalse(genMojo.isVerbose());
    assertTrue(genMojo.isHideBanner());
    assertEqualsPath("some/sources", genMojo.getSources(false).getPath());
    assertEqualsPath("some/root", genMojo.getGoRoot());
    assertEqualsPath("some/path", genMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},genMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},genMojo.getBuildFlags());
  }

  @Test
  public void testGolangInstallMojoConfiguration() throws Exception {
    final GolangInstallMojo instMojo = findMojo(GolangInstallMojo.class,"mojoInstall.xml","install");
    assertFalse(instMojo.isUseMavenProxy());
    assertFalse(instMojo.isSkip());
    assertNull(instMojo.getProxy());    
    assertFalse(instMojo.isUseEnvVars());
    assertNull(instMojo.getTargetArm());
    assertEquals("install",instMojo.getGoCommand());
    assertFalse(instMojo.isVerbose());
    assertTrue(instMojo.isHideBanner());
    assertEqualsPath("some/sources", instMojo.getSources(false).getPath());
    assertEqualsPath("some/root", instMojo.getGoRoot());
    assertEqualsPath("some/path", instMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},instMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},instMojo.getBuildFlags());
  }

  @Test
  public void testGolangVetMojoConfiguration() throws Exception {
    final GolangVetMojo vetMojo = findMojo(GolangVetMojo.class, "mojoVet.xml","vet");
    assertFalse(vetMojo.isUseMavenProxy());
    assertFalse(vetMojo.isSkip());
    assertNull(vetMojo.getProxy());
    assertFalse(vetMojo.isUseEnvVars());
    assertNull(vetMojo.getTargetArm());
    assertEquals("vet",vetMojo.getGoCommand());
    assertFalse(vetMojo.isVerbose());
    assertTrue(vetMojo.isHideBanner());
    assertEqualsPath("some/sources", vetMojo.getSources(false).getPath());
    assertEqualsPath("some/root", vetMojo.getGoRoot());
    assertEqualsPath("some/path", vetMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},vetMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},vetMojo.getBuildFlags());
  }

  @Test
  public void testGolangTestMojoConfiguration() throws Exception {
    final GolangTestMojo testMojo = findMojo(GolangTestMojo.class, "mojoTest.xml", "test");
    assertFalse(testMojo.isUseMavenProxy());
    assertNull(testMojo.getProxy());
    assertFalse(testMojo.isSkip());
    assertFalse(testMojo.isUseEnvVars());
    assertNull(testMojo.getTargetArm());
    assertEquals("test",testMojo.getGoCommand());
    assertFalse(testMojo.isVerbose());
    assertTrue(testMojo.isHideBanner());
    assertEqualsPath("some/sources", testMojo.getSources(false).getPath());
    assertEqualsPath("some/root", testMojo.getGoRoot());
    assertEqualsPath("some/path", testMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},testMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},testMojo.getBuildFlags());
    assertArrayEquals(new String[]{"binFlag1","binFlag2"},testMojo.getTestFlags());
    assertTrue(testMojo.isIgnoreErrorExitCode());
  }

  @Test
  public void testGolangToolMojoConfiguration() throws Exception {
    final GolangToolMojo toolMojo = findMojo(GolangToolMojo.class, "mojoTool.xml", "tool");
    assertFalse(toolMojo.isUseMavenProxy());
    assertNull(toolMojo.getProxy());
    assertFalse(toolMojo.isSkip());
    assertFalse(toolMojo.isUseEnvVars());
    assertNull(toolMojo.getTargetArm());
    assertEquals("tool",toolMojo.getGoCommand());
    assertFalse(toolMojo.isVerbose());
    assertTrue(toolMojo.isHideBanner());
    assertEqualsPath("some/sources", toolMojo.getSources(false).getPath());
    assertEquals("theCommand", toolMojo.getCommand());
    assertEqualsPath("some/root", toolMojo.getGoRoot());
    assertEqualsPath("some/path", toolMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"theCommand","arg1","arg2"},toolMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},toolMojo.getBuildFlags());
    assertArrayEquals(new String[]{"arg1","arg2"},toolMojo.getArgs());
  }

  @Test
  public void testGolangGetMojoConfiguration() throws Exception {
    final GolangGetMojo getMojo = findMojo(GolangGetMojo.class, "mojoGet.xml", "get");
    
    final ProxySettings proxy = getMojo.getProxy();
    
    assertEquals("https",proxy.protocol);
    assertEquals("127.33.44.55",proxy.host);
    assertEquals(999,proxy.port);
    assertEquals("some user",proxy.username);
    assertEquals("verysecretpassword",proxy.password);
    assertEquals("127.0.0.1|127.0.0.2|127.0.0.3",proxy.nonProxyHosts);
    
    assertEquals("some/custom/exe.exe",getMojo.getCvsExe());
    assertFalse(getMojo.isUseMavenProxy());
    assertFalse(getMojo.isSkip());
    assertFalse(getMojo.isUseEnvVars());
    assertEquals("get",getMojo.getGoCommand());
    assertNull(getMojo.getUseGoTool());
    assertNull(getMojo.getTargetArm());
    assertTrue(getMojo.isAutoFixGitCache());
    assertFalse(getMojo.isVerbose());
    assertTrue(getMojo.isHideBanner());
    assertEqualsPath("some/sources", getMojo.getSources(false).getPath());
    assertEqualsPath("some/root", getMojo.getGoRoot());
    assertEqualsPath("some/path", getMojo.findGoPath(false).getPath());
    assertArrayEquals(new String[]{"one_pack","two_pack"},getMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},getMojo.getBuildFlags());
    assertEquals("bin",getMojo.getExecSubpath());
    assertEquals("go",getMojo.getExec());
  }

  @Test
  public void testGolangBuildMojoConfiguration() throws Exception {
    final GolangBuildMojo buildMojo = findMojo(GolangBuildMojo.class,"mojoBuild.xml","build");
    assertTrue(buildMojo.isUseMavenProxy());
    assertNull(buildMojo.getProxy());
    assertNotNull(buildMojo);
    assertTrue(buildMojo.isSkip());
    assertEquals("somearch",buildMojo.getTargetArch());
    assertEquals("someos",buildMojo.getTargetOS());
    assertEquals("5566677",buildMojo.getTargetArm());
    assertFalse(buildMojo.isUseEnvVars());
    assertEquals("build", buildMojo.getGoCommand());
    assertEquals("someGo.bat",buildMojo.getUseGoTool());
    assertFalse(buildMojo.isVerbose());
    assertFalse(buildMojo.isHideBanner());
    assertEqualsPath("some/sources", buildMojo.getSources(false).getPath());
    assertEqualsPath("some/root", buildMojo.getGoRoot());
    assertEqualsPath("some/path", buildMojo.findGoPath(false).getPath());
    assertEqualsPath("target/place", buildMojo.getResultFolder());
    assertEquals("targetName", buildMojo.getResultName());
    assertArrayEquals(new String[]{"one_pack","two_pack"},buildMojo.getTailArguments());
    assertArrayEquals(new String[]{"flag1","flag2"},buildMojo.getBuildFlags());
    assertEquals(1,buildMojo.getEnv().size());
    assertEquals("somevalue",buildMojo.getEnv().get("somekey"));
    assertEquals("bin/misc", buildMojo.getExecSubpath());
    assertEquals("gomobile", buildMojo.getExec());
  }

}
