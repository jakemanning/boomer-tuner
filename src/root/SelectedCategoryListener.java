package root;

import utils.CategoryType;

/**
 * Created by bryancapps on 4/4/17.
 */
@FunctionalInterface
public interface SelectedCategoryListener {
    void selectedCategoryChanged(CategoryType value);
}
