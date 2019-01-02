package com.softwerke.api;

/**
 * Interface to NewsResource service
 *
 * @author nordenInc
 *
 */

public interface NewsResource {

    /**
     * These methods will be used for commands {news:stats}, {news:stats <param>}  in class News for showing available
     * news sources and getting top news titles for them.
     */
    public String getName();

    /**
     * Returns type of concrete source implementation.
     *
     * @return type for specific resources
     */
    public String getType();

    /**
     * Returns news titles for chosen resource.
     *
     * @return array of titles
     * @throws Exception
     */
    public String[] getNewsTitles() throws Exception;

    /**
     * Returns news titles for URL that was entered as a param by user.
     *
     * @param clientUrl
     * @return array of titles
     * @throws Exception
     */
    public String[] getNewsTitles(String clientUrl) throws Exception;

}