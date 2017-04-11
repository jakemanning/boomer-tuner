package root;

import utils.CategoryType;

@FunctionalInterface
public interface SelectedCategoryListener {
    void selectedCategoryChanged(CategoryType value);
}
