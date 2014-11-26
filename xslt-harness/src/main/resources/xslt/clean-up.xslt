<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:strip-space elements="*"/>

	<xsl:template match="/">
		<xsl:variable name="pass1">
			<xsl:apply-templates mode="first"/>
		</xsl:variable>

		<xsl:apply-templates select="$pass1" mode="second"/>
	</xsl:template>

	<xsl:template match="*[text()[starts-with(.,'${')]]" mode="first">
		<xsl:if test="count(ancestor::*) &lt;= 3">
			<xsl:element name="{local-name()}"/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="*[not(@*) and not(node())]" mode="second">
		<xsl:if test="count(ancestor::*) &lt; 3">
			<xsl:copy>
				<xsl:apply-templates select="node()|@*" mode="second"/>
			</xsl:copy>
		</xsl:if>
		<xsl:message select="concat(' - ', local-name())"/>
	</xsl:template>

	<xsl:template match="node()|@*" priority="-1" mode="#all">
	    <xsl:copy>
	        <xsl:apply-templates select="node()|@*" mode="#current"/>
	    </xsl:copy>
	</xsl:template>
</xsl:stylesheet>
