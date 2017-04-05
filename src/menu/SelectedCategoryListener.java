package menu;

import utils.Category;

/**
 * Created by bryancapps on 4/4/17.
 */
@FunctionalInterface
public interface SelectedCategoryListener {
    void selectedCategoryChanged(Category value);
}
